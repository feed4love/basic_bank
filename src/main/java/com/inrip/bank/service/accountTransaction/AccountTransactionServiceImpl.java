package com.inrip.bank.service.accountTransaction;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.inrip.bank.dto.AccountRequestDTO;
import com.inrip.bank.dto.AccountResponseDTO;
import com.inrip.bank.dto.AccountTransactionRequestDTO;
import com.inrip.bank.dto.AccountTransactionResponseDTO;
import com.inrip.bank.model.AccountTransaction;

import com.inrip.bank.common.SimpleBankUtils;
import com.inrip.bank.controller.exceptions.SimpleBankBadRequestException;
import com.inrip.bank.controller.exceptions.SimpleBankNotFoundException;
import com.inrip.bank.repository.AccountTransactionRepository;
import com.inrip.bank.service.account.AccountService;

import com.inrip.bank.controller.exceptions.ResourceNotFoundException;
/**
 * @author Enrique AC
 * 
 * Main service for transaction in the test:
 * -> addTransaction
 * -> getTransactionByAccountIban
 * -> getTransactionByReference
 *
 */
@Service
public class AccountTransactionServiceImpl implements AccountTransactionService {

	private static final Logger mLogger = LogManager.getLogger(AccountTransactionServiceImpl.class);

	@Autowired
	private AccountTransactionRepository mTransactionRepository;

	@Autowired
	private AccountService mAccountService;
	
	@Value("${com.inrip.bank.param.create_account_iban_if_not_exists}")
	private boolean PARAM_ASSUMPTION_ACCOUNT_IBAN_SHALL_EXISTS;
	
	@Value("${com.inrip.bank.param.security.max_items_size_protection}")
	private int PARAM_MAX_ITEMS_PROTECTION;

	/*
	 * List all transactions
	 */
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<AccountTransactionResponseDTO> getAllTransactions() {
		mLogger.info("Init - list all transactions");
		AccountTransactionResponseDTO responseDTO;
		List<AccountTransaction> listTransactions;
		List<AccountTransactionResponseDTO> listTransactionsResponseDTO;

		listTransactions = mTransactionRepository.findAll();
		listTransactionsResponseDTO = AccountTransactionTransformer.listTransactionToResponseDTO(listTransactions);

		mLogger.info("End- transactions recovered <" + listTransactionsResponseDTO.size() + ">");
		return listTransactionsResponseDTO;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public AccountTransactionResponseDTO addTransaction(AccountTransactionRequestDTO transactionRequestDTO) throws Exception  {		
		AccountTransactionResponseDTO responseDTO = null;
		AccountTransaction            transaction = null;
		String                 generatedReference = null;

		mLogger.info("Init - create transaction");
		AccountTransactionLogicalValidator.validateTransactionRequest(transactionRequestDTO);

		try {

			//main account associated to the account_iban
			AccountResponseDTO mAccount = null;

			/*
			 * BLOQUE ACCOUNT: Get the account to done credit
			 * 
			 */
			AccountRequestDTO accountRequestDTO = AccountRequestDTO.Builder.newInstance()
													.setAccountiban(transactionRequestDTO.getAccount_iban())
													.build();

			Optional<AccountResponseDTO> account = mAccountService.findAccountByAccountIban(accountRequestDTO);
			if(account.isPresent()) {
				mLogger.debug("Account exists <" + account.get().toString() + ">");
			}else
			if(PARAM_ASSUMPTION_ACCOUNT_IBAN_SHALL_EXISTS){
				mLogger.debug("Account do nto exists , but created");

				AccountRequestDTO newAccountRequestDTO = AccountRequestDTO.Builder.newInstance()
														.setAccountiban(transactionRequestDTO.getAccount_iban())
														.setCredit(Double.valueOf(0))
														.build();

				Optional<AccountResponseDTO> tmpAccount = mAccountService.addAccount(newAccountRequestDTO);
				if(tmpAccount.isPresent())
					mAccount = tmpAccount.get();
				else
					throw new SimpleBankBadRequestException("ACCOUNT_OUTOFSYNCH", "Need synchronize Accounts");
			}

			/*
			 * Check if the operation is allowed by the Account Service (done into Account service <validateForAcceptTransaction>)
			 * Reques:
			 * 	1) amount (mandatory): If positive the transaction is a credit (add money) to the
			 *	   account. If negative it is a debit (deduct money from the account)
			 *  2) IMPORTANT to note that a transaction that leaves the total account balance bellow
		     *     0 is not allowed.
			 */
			mAccountService.updateCreditByTransactionRequest(transactionRequestDTO);

			/*
			 * BLOQUE TRANSACCION
			 *   register the transaction
			 * 
			 */
			//throws exception
			transaction = AccountTransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);
			if(transaction.getReference()==null || transaction.getReference().trim().length()<=0){
				generatedReference = SimpleBankUtils.GenerateUUID();
				transaction.setReference(generatedReference);
			}else{				
				Optional<AccountTransaction> tmpTransactionDTO = this.getTransactionByReference(transaction.getReference());
				if (tmpTransactionDTO.isPresent()) {
					throw new SimpleBankBadRequestException("INVALID_REFERENCE", "The provided Field<reference> is not valid");
				}
			}

			//save to bbdd
			String UUID = SimpleBankUtils.GenerateUUID();
			transaction.setUid(UUID);

			transaction = (AccountTransaction) mTransactionRepository.save(transaction);
			transaction.setUid(UUID);
			if(generatedReference!=null)
				transaction.setReference(generatedReference);

		}catch(SimpleBankBadRequestException br) {
			mLogger.info("Error found : " + br.getMessage().toString());			
			throw br;
		}catch (SimpleBankNotFoundException nf) {
			mLogger.info("Server Error found : " + nf.getMessage().toString());			
			throw nf;
		}catch (HttpServerErrorException hse) {
			mLogger.error("Server Error found : " + hse.getMessage().toString());
			throw hse;
		}catch (Exception e) {
			mLogger.error("Error found : " + e.getMessage().toString());			
			throw e;
		}		
		responseDTO = AccountTransactionTransformer.transactionToResponseDto(transaction);
		mLogger.info("End - Successfully add the transaction");
		return responseDTO;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<AccountTransactionResponseDTO> getTransactionByAccountIban(String strAccountIban, int page, int size, boolean descending_amount) {		
		mLogger.info("Init - getTransactionByAccountIban");
		List<AccountTransactionResponseDTO> listTransactionsResponseDTO;

		if(size>PARAM_MAX_ITEMS_PROTECTION)
			throw new ResourceNotFoundException("SIZE_NOT_ALLOWED","Size up to 20 for a page is not allowed",page);

		Direction direction = Direction.ASC;
		if (descending_amount)
			direction = Direction.DESC;

		Page<AccountTransaction> pageAccountTransaction = mTransactionRepository.findAllByAccountiban(
									strAccountIban, 
									PageRequest.of(page, size, Sort.by(direction, "amount")));

		if (page > pageAccountTransaction.getTotalPages())
				throw new ResourceNotFoundException("END_OF_LIST", "Page", page);


		listTransactionsResponseDTO = AccountTransactionTransformer.pagedTransactionToResponseDTO(pageAccountTransaction);

		mLogger.info("End - Successfully returned <" + listTransactionsResponseDTO.size() + "> transactions by account_iban");
		return listTransactionsResponseDTO;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<AccountTransactionResponseDTO> getAllTransactionByReference(String strReference, boolean descending_amount) {
		List<AccountTransaction> listTransactions = null;
		List<AccountTransactionResponseDTO> listTransactionsResponseDTO;

		Direction direction = Direction.ASC;
		if (descending_amount)
			direction = Direction.DESC;
		listTransactions = mTransactionRepository.findByReference(strReference, Sort.by(direction, "id"));

		listTransactionsResponseDTO = AccountTransactionTransformer.listTransactionToResponseDTO(listTransactions);
		
		return listTransactionsResponseDTO;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public Optional<AccountTransaction> getTransactionByReference(String strReference){
		Optional<AccountTransaction> optTransaction = null;
		optTransaction = mTransactionRepository.findByReference(strReference);
		return optTransaction;
	}

}

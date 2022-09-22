package com.inrip.bank.service.transaction;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.inrip.bank.dto.AccountRequestDTO;
import com.inrip.bank.dto.AccountResponseDTO;
import com.inrip.bank.dto.TransactionRequestDTO;
import com.inrip.bank.dto.TransactionResponseDTO;
import com.inrip.bank.model.Transaction;

import com.inrip.bank.common.Utils;
import com.inrip.bank.controller.exceptions.BadRequestException;
import com.inrip.bank.controller.exceptions.NotFoundException;
import com.inrip.bank.repository.TransactionRepository;
import com.inrip.bank.service.account.AccountService;


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
public class TransactionServiceImpl implements TransactionService {

	private static final Logger mLogger = LogManager.getLogger(TransactionServiceImpl.class);

	@Autowired
	private TransactionRepository mTransactionRepository;

	@Autowired
	private AccountService mAccountService;
	
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.mTransactionRepository = transactionRepository;
    }

	@Value("${bank.basic.ASSUMPTION_ACCOUNT_IBAN_SHALL_EXISTS}")
	private boolean PARAM_ASSUMPTION_ACCOUNT_IBAN_SHALL_EXISTS;

	
	/*
	 * List all transactions
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TransactionResponseDTO> getAllTransactions() {
		mLogger.info("Init - list all transactions");
		TransactionResponseDTO responseDTO;
		List<Transaction> listTransactions;
		List<TransactionResponseDTO> listTransactionsResponseDTO;

		listTransactions = mTransactionRepository.findAll();
		listTransactionsResponseDTO = TransactionTransformer.listTransactionToResponseDTO(listTransactions);

		mLogger.info("End- transactions recovered <" + listTransactionsResponseDTO.size() + ">");
		return listTransactionsResponseDTO;
	}

	@Override
	@Transactional
	public TransactionResponseDTO addTransaction(TransactionRequestDTO transactionRequestDTO) throws Exception  {		
		TransactionResponseDTO responseDTO = null;
		Transaction            transaction = null;
		String                 generatedReference = null;

		mLogger.info("Init - create transaction");
		TransactionLogicalValidator.validateTransactionRequest(transactionRequestDTO);

		try {

			//main account associated to the account_iban
			AccountResponseDTO mAccount = null;

			/*
			 * BLOQUE ACCOUNT: Get the account to done credit
			 * 
			 */
			AccountRequestDTO accountRequest = new AccountRequestDTO();
			accountRequest.setAccountiban(transactionRequestDTO.getAccount_iban());
			Optional<AccountResponseDTO> account = mAccountService.findAccountByAccountIban(accountRequest);
			if(account.isPresent()) {
				mLogger.debug("Account exists <" + account.get().toString() + ">");
			}else
			if(PARAM_ASSUMPTION_ACCOUNT_IBAN_SHALL_EXISTS){
				mLogger.debug("Account do nto exists , but created");
				AccountRequestDTO newAccountRequest = new AccountRequestDTO();
				newAccountRequest.setAccountiban(transactionRequestDTO.getAccount_iban());
				newAccountRequest.setCredit(Double.valueOf(0));
				
				Optional<AccountResponseDTO> tmpAccount = mAccountService.addAccount(newAccountRequest);
				if(tmpAccount.isPresent())
					mAccount = tmpAccount.get();
				else
					throw new BadRequestException("ACCOUNT_OUTOFSYNCH", "Need synchronize Accounts");
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
			transaction = TransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);
			if(transaction.getReference()==null || transaction.getReference().trim().length()<=0){
				generatedReference = Utils.GenerateUUID();
				transaction.setReference(generatedReference);
			}else{				
				Optional<Transaction> tmpTransactionDTO = this.getTransactionByReference(transaction.getReference());
				if (tmpTransactionDTO.isPresent()) {
					throw new BadRequestException("INVALID_REFERENCE", "The provided Field<reference> is not valid");
				}
			}

			//save to bbdd
			String UUID = Utils.GenerateUUID();
			transaction.setUid(UUID);			
			transaction = (Transaction) mTransactionRepository.save(transaction);

			//patch test cases, if mockito mocks save method, reasign the uuid and the reference generated
			transaction.setUid(UUID);
			if(generatedReference!=null)
				transaction.setReference(generatedReference);

		}catch(BadRequestException br) {
			mLogger.info("Error found : " + br.getMessage().toString());			
			throw br;
		}catch (NotFoundException nf) {
			mLogger.info("Server Error found : " + nf.getMessage().toString());			
			throw nf;
		}catch (HttpServerErrorException hse) {
			mLogger.error("Server Error found : " + hse.getMessage().toString());
			throw hse;
		}catch (Exception e) {
			mLogger.error("Error found : " + e.getMessage().toString());			
			throw e;
		}		
		responseDTO = TransactionTransformer.transactionToResponseDto(transaction);
		mLogger.info("End - Successfully add the transaction");
		return responseDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TransactionResponseDTO> getTransactionByAccountIban(String strAccountIban, boolean descending_amount) {		
		mLogger.info("Init - getTransactionByAccountIban");
		List<TransactionResponseDTO> listTransactionsResponseDTO;
		List<Transaction>            listTransaction = null;
		
		Direction direction = Direction.ASC;
		if (descending_amount)
			direction = Direction.DESC;

		listTransaction             = mTransactionRepository.findAllByAccountiban(strAccountIban, Sort.by(direction, "amount"));
		listTransactionsResponseDTO = TransactionTransformer.listTransactionToResponseDTO(listTransaction);

		mLogger.info("End - Successfully returned <" + listTransactionsResponseDTO.size() + "> transactions by account_iban");
		return listTransactionsResponseDTO;
	}

	@Transactional(readOnly = true)
	public List<TransactionResponseDTO> getAllTransactionByReference(String strReference, boolean descending_amount) {
		List<Transaction> listTransactions = null;
		List<TransactionResponseDTO> listTransactionsResponseDTO;

		Direction direction = Direction.ASC;
		if (descending_amount)
			direction = Direction.DESC;
		listTransactions = mTransactionRepository.findByReference(strReference, Sort.by(direction, "id"));

		listTransactionsResponseDTO = TransactionTransformer.listTransactionToResponseDTO(listTransactions);
		
		return listTransactionsResponseDTO;
	}

	@Transactional(readOnly = true)
	public Optional<Transaction> getTransactionByReference(String strReference){
		Optional<Transaction> optTransaction = null;
		optTransaction = mTransactionRepository.findByReference(strReference);
		return optTransaction;
	}


	/*public List<Transaction> findByReferenceASC(String strReference) {
		List<Transaction> listTransactions = null;
		listTransactions = mTransactionRepository.findByReference(strReference, Sort.by(Direction.ASC, "id"));
		return listTransactions;
	}

	public List<Transaction> findByReferenceDESC(String strReference) {
		List<Transaction> listTransactions = null;
		listTransactions = mTransactionRepository.findByReference(strReference, Sort.by(Direction.DESC, "id"));
		return listTransactions;
	}*/


}

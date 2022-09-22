package com.inrip.bank.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inrip.bank.common.Utils;
import com.inrip.bank.dto.AccountRequestDTO;
import com.inrip.bank.dto.AccountResponseDTO;
import com.inrip.bank.dto.TransactionRequestDTO;
import com.inrip.bank.model.Account;
import com.inrip.bank.repository.AccountRepository;


@Service
public class AccountServiceImpl  implements AccountService {

    private static final Logger mLogger = LogManager.getLogger(AccountServiceImpl.class);

	@Autowired
	private AccountRepository mAccountRepository;

    @Value("${bank.basic.ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS}")
	private boolean PARAM_ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public AccountResponseDTO updateCreditByTransactionRequest(TransactionRequestDTO transactionRequestDTO) throws Exception {
        AccountResponseDTO responseAccount = null;
        Optional<Account> optAccount = null;    

        mLogger.info("Init - updateCreditByTransactionRequest");
        Assert.notNull(transactionRequestDTO.getAccount_iban(), "account_iban is mandatory for the operation");
        
        /*
         * get account or exception
         */
        optAccount = mAccountRepository.findOneByAccountiban(transactionRequestDTO.getAccount_iban());
        Account account = optAccount.get();
        Assert.notNull(account, "The request is not valid or it is out of scope");

        /*
         * check filter validator before continue to save credit, or BadRequestException
         */
        Double dCreditAfterTransaction = AccountLogicalValidator.validateToAcceptTransaction(
                                            account, 
                                            transactionRequestDTO, 
                                            PARAM_ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS);

        /*
         * save credit
         */
        account.setCredit(dCreditAfterTransaction);        
        account = (Account) mAccountRepository.save(account);

        //return
        responseAccount = AccountTransformer.accountToResponseDTO(account);
        mLogger.info("End - updateCreditByTransactionRequest");
        return responseAccount;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Optional<AccountResponseDTO> addAccount(AccountRequestDTO accountRequestDTO) {
        mLogger.info("Init - addAccount");
        Assert.hasText(accountRequestDTO.getAccountiban(), "cant create an account without the account_iban");
        AccountResponseDTO accountResponseDTO = null;

        Account account = AccountTransformer.accountRequestDtoToAccount(accountRequestDTO);
        account.setUid(Utils.GenerateUUID());
        account = (Account) mAccountRepository.save(account);

        accountResponseDTO = AccountTransformer.accountToResponseDTO(account);        
        mLogger.info("End - addAccount");
        return Optional.of(accountResponseDTO);
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Optional<AccountResponseDTO> findAccountByAccountIban(AccountRequestDTO accountRequestDTO) {
        Optional<Account> optAccount = null;        
        AccountResponseDTO accountResponseDTO = null;
        Optional<AccountResponseDTO> optAccountResponseDTO = Optional.empty();
        
		mLogger.info("Init - findAccountByAccountIban");
		
        //throws exception
        AccountLogicalValidator.validateAccountRequest(accountRequestDTO);

        optAccount = mAccountRepository.findOneByAccountiban(accountRequestDTO.getAccountiban());

        if(optAccount.isPresent()) {
            accountResponseDTO = AccountTransformer.accountToResponseDTO(optAccount.get());
            optAccountResponseDTO = Optional.of(accountResponseDTO);
        }   

		mLogger.info("End - findAccountByAccountIban");
		return optAccountResponseDTO;
    }


    
}

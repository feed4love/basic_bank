package com.inrip.bank.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    @Transactional
    public AccountResponseDTO updateCreditByTransactionRequest(TransactionRequestDTO transactionRequestDTO) {
        AccountResponseDTO responseAccount = null;
        Optional<Account> optAccount = null;    

        mLogger.info("Init - updateCreditByTransactionRequest");
        Assert.notNull(transactionRequestDTO.getAccount_iban(), "account_iban is mandatory for the operation");
        
        /*
         * obtenemos la cuenta, si no se encuentra se lanza excepcion
         */
        optAccount = mAccountRepository.findOneByAccountiban(transactionRequestDTO.getAccount_iban());
        Account account = optAccount.get();
        Assert.notNull(account, "No esxiste cuenta asociada o la peticion se esta realizando fuera del contexto de una transaccion");

        /*
         * si hay cuenta , pasamos los filtros para validar la trsaccion, idenpendientemente de las comprobacions de otros servicios
         * obtenemos el resultado que es el credito final de la cuenta descontando la transaccion
         */
        AccountLogicalValidator.validateToAcceptTransaction(account, transactionRequestDTO, 
                                        PARAM_ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS);

        /*
         * salvamos el nuevo credito y retornamos
         */
        Double dCreditAfterTransaction = AccountLogicalValidator.obtainCreditAfterTransaction(account, 
                                        transactionRequestDTO, PARAM_ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS);

        account.setCredit(dCreditAfterTransaction);        
        account = (Account) mAccountRepository.save(account);

        //preparamos al salida
        responseAccount = AccountTransformer.accountToResponseDTO(account);

        mLogger.info("End - updateCreditByTransactionRequest");
        return responseAccount;
    }

    @Override
    @Transactional
    public Optional<AccountResponseDTO> addAccount(AccountRequestDTO accountRequestDTO) {
        mLogger.info("Init - addAccount");
        Assert.hasText(accountRequestDTO.getAccountiban(), "cant create an account without the account_iban");
        AccountResponseDTO accountResponseDTO = null;

        Account account = AccountTransformer.accountRequestDtoToAccount(accountRequestDTO);
        account = (Account) mAccountRepository.save(account);

        accountResponseDTO = AccountTransformer.accountToResponseDTO(account);        
        mLogger.info("End - addAccount");
        return Optional.of(accountResponseDTO);
    }


    @Override
    @Transactional(readOnly = true)
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

package com.inrip.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.inrip.bank.service.accountTransaction.AccountTransactionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class ApplicationListenerInitialize implements ApplicationListener<ApplicationReadyEvent>  {
     
    @Autowired
    AccountTransactionService mTransactionService;

    private static final Logger mLogger = LogManager.getLogger(ApplicationListenerInitialize.class);
 
    public void onApplicationEvent(ApplicationReadyEvent event) {
        mLogger.info("Spring boot inizialization OK");

        try {
            //code for internal tests
            //init H2 database with 3 records for date yesterday, today and tomorrow
            /*TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO();
            transactionRequestDTO.setReference("REF-001");
            transactionRequestDTO.setAccount_iban("IBAN-001");
            transactionRequestDTO.setDescription("Transaccion #1");
            transactionRequestDTO.setDate(Utils.getToday(true));
            transactionRequestDTO.setAmount(new BigDecimal(10));                
            transactionRequestDTO.setFee(new BigDecimal(2));    
            mTransactionService.addTransaction(transactionRequestDTO);

            transactionRequestDTO = new TransactionRequestDTO();
            transactionRequestDTO.setReference("REF-002");
            transactionRequestDTO.setAccount_iban("IBAN-001");
            transactionRequestDTO.setDescription("Transaccion #2");
            transactionRequestDTO.setDate(Utils.getTomorrow(true));
            transactionRequestDTO.setAmount(new BigDecimal(8));
            transactionRequestDTO.setFee(new BigDecimal(2));    
            mTransactionService.addTransaction(transactionRequestDTO);

            transactionRequestDTO = new TransactionRequestDTO();
            transactionRequestDTO.setReference("REF-003");
            transactionRequestDTO.setAccount_iban("IBAN-001");
            transactionRequestDTO.setDescription("Transaccion #3");
            transactionRequestDTO.setDate(Utils.getYesterday(true));
            transactionRequestDTO.setAmount(new BigDecimal(14));
            transactionRequestDTO.setFee(new BigDecimal(3));
            mTransactionService.addTransaction(transactionRequestDTO);*/
            
        } catch (Exception e) {            
            mLogger.error(e.getMessage());
        }
        
    }
}
package com.inrip.bank.common;

import java.util.Date;
import java.util.Optional;

import com.inrip.bank.dto.AccountTransactionStatusRequestDTO;
import com.inrip.bank.dto.AccountTransactionRequestDTO;
import com.inrip.bank.model.AccountTransaction;

public class UtilTest {
    
    protected static String DEF_TEST_REFERENCE    = "REF-TEST-001";
    protected static String DEF_TEST_ACCOUNT_IBAN = "IBAN-TEST-001";

    public enum TRANSACTION_CHANNEL  {
        CLIENT("CLIENT"), 
        ATM("ATM"), 
        INTERNAL("INTERNAL");    
        private String channel;    
        TRANSACTION_CHANNEL(String channel) {
            this.channel = channel;
        }    
        public String getTRANSACTION_CHANNEL() {
            return channel;
        }
    }

    public enum TRANSACTION_STATUS  {
        INVALID("INVALID"),
        SETTLED("SETTLED"),
        PENDING("PENDING"),
        FUTURE("FUTURE");
        private String status;    
        TRANSACTION_STATUS(String status) {
            this.status = status;
        }    
        public String get() {
            return status;
        }
    }

    public enum TRANSACTION_WHEN {
        TODAY,
        TOMORROW,
        YESTERDAY,
        NO_DATE
    }

    public static AccountTransactionStatusRequestDTO getFakeStatusRequestDTO(TRANSACTION_CHANNEL channel) {
        AccountTransactionStatusRequestDTO statusRequestDTO = AccountTransactionStatusRequestDTO.Builder.newInstance()
                                                                .setReference(UtilTest.DEF_TEST_REFERENCE)
                                                                .setChannel(channel.getTRANSACTION_CHANNEL())
                                                                .build();
        /*AccountTransactionStatusRequestDTO statusRequestDTO = new AccountTransactionStatusRequestDTO();
        statusRequestDTO.setReference(UtilTest.DEF_TEST_REFERENCE);
        statusRequestDTO.setChannel(channel.getTRANSACTION_CHANNEL());*/
        return statusRequestDTO;
    }

    public static Optional<AccountTransaction> getFakeOptionalTransaction(
                           TRANSACTION_WHEN when, 
                           boolean trucate_dates, 
                           Double amount, Double fee) {
        AccountTransaction transaction = new AccountTransaction();
        transaction.setReference(DEF_TEST_REFERENCE);
        transaction.setAccountiban(DEF_TEST_ACCOUNT_IBAN);                
        transaction.setAmount(amount);
        transaction.setFee(fee);
        if(when == TRANSACTION_WHEN.TOMORROW){
            transaction.setDate(SimpleBankUtils.getTomorrow(trucate_dates));
        }else
        if(when==TRANSACTION_WHEN.YESTERDAY){
            transaction.setDate(SimpleBankUtils.getYesterday(trucate_dates));
        }else
        if(when==TRANSACTION_WHEN.NO_DATE){
            transaction.setDate(null);
        }else{
            transaction.setDate(SimpleBankUtils.getToday(trucate_dates));
        }
        return Optional.of(transaction);
    }    

    public static AccountTransactionRequestDTO getFakeTransactionRequestDTO(boolean account_iban, boolean reference) {
        return getFakeTransactionRequestDTO(account_iban, reference, TRANSACTION_WHEN.TODAY, false, 10.0, 2.0);
    }
    public static AccountTransactionRequestDTO getFakeTransactionRequestDTO(
                                  boolean account_iban, 
                                  boolean reference,
                                  TRANSACTION_WHEN when,
                                  boolean trucate_dates, 
                                  double amount, 
                                  double fee) {

        String txt_acount_iban = null, txt_reference = null;

        if(account_iban)
            txt_acount_iban = DEF_TEST_ACCOUNT_IBAN;
        if(reference)
            txt_reference = DEF_TEST_REFERENCE;
                            
        Date date = null;
        if(when == TRANSACTION_WHEN.TOMORROW){
            date = SimpleBankUtils.getTomorrow(trucate_dates);
        }else
        if(when==TRANSACTION_WHEN.YESTERDAY){
            date = SimpleBankUtils.getYesterday(trucate_dates);            
        }else
        if(when==TRANSACTION_WHEN.NO_DATE){
            date = null;            
        }else{
            date = SimpleBankUtils.getToday(trucate_dates);
        }
                            
        AccountTransactionRequestDTO transactionRequestDTO = AccountTransactionRequestDTO.Builder.newInstance()
                                            .setAccount_iban(txt_acount_iban)
                                            .setReference(txt_reference)
                                            .setAmount(amount)
                                            .setFee(Double.valueOf(fee))
                                            .setDate(date)
                                            .build();

        /*AccountTransactionRequestDTO transactionRequestDTO = new AccountTransactionRequestDTO();
        transactionRequestDTO.setAccount_iban(DEF_TEST_ACCOUNT_IBAN);
        transactionRequestDTO.setReference(DEF_TEST_REFERENCE);
        transactionRequestDTO.setAmount(amount);        
        transactionRequestDTO.setFee(Double.valueOf(fee));*/
        /*if(when == TRANSACTION_WHEN.TOMORROW){
            transactionRequestDTO.setDate(SimpleBankUtils.getTomorrow(trucate_dates));            
        }else
        if(when==TRANSACTION_WHEN.YESTERDAY){
            transactionRequestDTO.setDate(SimpleBankUtils.getYesterday(trucate_dates));
        }else
        if(when==TRANSACTION_WHEN.NO_DATE){
            transactionRequestDTO.setDate(null);
        }else{
            transactionRequestDTO.setDate(SimpleBankUtils.getToday(trucate_dates));
        }*/
        return transactionRequestDTO;
    }    


}

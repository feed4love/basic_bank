package com.inrip.bank.service.accountTransaction;

import org.springframework.util.Assert;

import com.inrip.bank.dto.AccountTransactionRequestDTO;

/**
 * @author Enrique AC
 *
 		• reference (optional): The transaction unique reference number in our system. If 
		not present, the system will generate one.
		• account_iban (mandatory): The IBAN number of the account where the 
		transaction has happened.
		• date (optional): Date when the transaction took place
		• amount (mandatory): If positive the transaction is a credit (add money) to the 
		account. If negative it is a debit (deduct money from the account)
		• fee (optional): Fee that will be deducted from the amount, regardless on the 
		amount being positive or negative.
		• description (optional): The description of the transaction
 * 
 */
public class AccountTransactionLogicalValidator {

	/**
	 * @param AccountTransactionRequestDTO
	 * 
 	 */
	public static void validateTransactionRequest(AccountTransactionRequestDTO request) {
		Assert.notNull(request, "Transaction Request cannot be null");		
		Assert.hasText(request.getAccount_iban(), "Account IBAN is required");
		Assert.notNull(request.getAmount(), "Amount is required");

		//ñapa: 
		//if(request.getFee()==null)
		//	request.setFee(Double.valueOf(0));

	}

}

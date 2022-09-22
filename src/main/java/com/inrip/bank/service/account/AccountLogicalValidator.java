package com.inrip.bank.service.account;

import org.springframework.util.Assert;

import com.inrip.bank.controller.exceptions.SimpleBankBadRequestException;
import com.inrip.bank.dto.AccountRequestDTO;
import com.inrip.bank.dto.AccountTransactionRequestDTO;
import com.inrip.bank.model.Account;

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
public class AccountLogicalValidator {

	/**
	 * @param AccountRequestDTO
	 * 
 	 */
	public static void validateAccountRequest(AccountRequestDTO request) {
		Assert.notNull(request, "Account Request cannot be null");		
		Assert.hasText(request.getAccountiban(), "Account IBAN is required");				
	}

	public static Double validateToAcceptTransaction(Account account, AccountTransactionRequestDTO transactionRequestDTO, boolean ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS) {
		Double dTotal = Double.valueOf(0);
		dTotal = ApplyCreditRule(transactionRequestDTO.getAmount(),
								 transactionRequestDTO.getFee(),
								 account.getCredit(), ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS);
		if(dTotal.doubleValue() < 0d) {
			throw new SimpleBankBadRequestException("ACCOUNT_NOMONEY", "A transaction that leaves the total account balance bellow 0 is not allowed");
		}
		return dTotal;
	}

	/*public static Double obtainCreditAfterTransaction(Account account, TransactionRequestDTO transactionRequestDTO, boolean ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS) {
		Double dTotal = Double.valueOf(0);
		dTotal = ApplyCreditRule(transactionRequestDTO.getAmount(),
								 transactionRequestDTO.getFee(),
								 account.getCredit(), ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS);
		return dTotal;
	}	*/

	private static Double ApplyCreditRule(Double dAmount, 
									      Double dFee, 
										  Double dCredit, 
										  boolean ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS) {
		double amount = 0, fee = 0, credit = 0, result = 0;
		if(ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS) {
			amount = (dAmount==null?0:dAmount.doubleValue());
			fee    = (dFee==null?0:dFee.doubleValue());
			credit = (dCredit==null?0:dCredit.doubleValue());
		}
		result  = (credit - fee) + amount;
		return Double.valueOf(result);
	}


}

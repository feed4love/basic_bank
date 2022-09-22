package com.inrip.bank.service.account;

import org.springframework.util.Assert;

import com.inrip.bank.controller.exceptions.BadRequestException;
import com.inrip.bank.dto.AccountRequestDTO;
import com.inrip.bank.dto.TransactionRequestDTO;
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

	public static void validateToAcceptTransaction(Account account, TransactionRequestDTO transactionRequestDTO, boolean ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS) {
		Double dTotal = Double.valueOf(0);
		if(ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS) {

			dTotal = ApplyCreditRule(Double.valueOf(transactionRequestDTO.getAmount()).doubleValue(),
			                         Double.valueOf(transactionRequestDTO.getFee()==null?0:transactionRequestDTO.getFee()).doubleValue(),
									 account.getCredit().doubleValue());

			if(dTotal.doubleValue() < 0d) {
				throw new BadRequestException("ACCOUNT_NOMONEY", "A transaction that leaves the total account balance bellow 0 is not allowed");
			}
		}
	}

	public static Double obtainCreditAfterTransaction(Account account, TransactionRequestDTO transactionRequestDTO, boolean ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS) {
		Double dTotal = Double.valueOf(0);
		if(ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS) {
			dTotal = ApplyCreditRule(Double.valueOf(transactionRequestDTO.getAmount()).doubleValue(),
			                         Double.valueOf(transactionRequestDTO.getFee()==null?0:transactionRequestDTO.getFee()).doubleValue(),
									 account.getCredit().doubleValue());
		}
		return dTotal;
	}	

	private static Double ApplyCreditRule(double amount, double fee, double credit) {
		double result  = (credit - fee) + amount;
		return Double.valueOf(result);
	}


}

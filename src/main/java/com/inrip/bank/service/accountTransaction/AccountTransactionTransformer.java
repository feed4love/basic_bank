package com.inrip.bank.service.accountTransaction;

import java.util.ArrayList;
import java.util.List;

import com.inrip.bank.dto.AccountTransactionRequestDTO;
import com.inrip.bank.dto.AccountTransactionResponseDTO;
import com.inrip.bank.model.AccountTransaction;

/**
 * @author Enrique AC
 * 
 */
public class AccountTransactionTransformer {

	
	public static List<AccountTransactionResponseDTO> listTransactionToResponseDTO(List<AccountTransaction> listTransactions) {
		List<AccountTransactionResponseDTO> listTransactionResponseDTO = new ArrayList<AccountTransactionResponseDTO>();		
		listTransactions.forEach(item -> listTransactionResponseDTO.add(AccountTransactionTransformer.transactionToResponseDto(item)));
		return listTransactionResponseDTO;
	}


	public static AccountTransaction transactionRequestDtoToTransaction(AccountTransactionRequestDTO transactionRequest) {
		AccountTransaction transaction = new AccountTransaction();		
		transaction.setReference(transactionRequest.getReference());
		transaction.setAccountiban(transactionRequest.getAccount_iban());
		transaction.setDate( transactionRequest.getDate() );
		transaction.setAmount(transactionRequest.getAmount());
		transaction.setFee(transactionRequest.getFee());
		transaction.setDescription(transactionRequest.getDescription());
		return transaction;
	}

	public static AccountTransactionResponseDTO transactionToResponseDto(AccountTransaction transaction) {
		AccountTransactionResponseDTO response = new AccountTransactionResponseDTO();						
		response.setReference(transaction.getReference());
		response.setAccount_iban(transaction.getAccountiban());
		response.setDate(transaction.getDate());
		response.setAmount(transaction.getAmount());
		response.setFee(transaction.getFee());
		response.setDescription(transaction.getDescription());
		return response;
	}
	

}

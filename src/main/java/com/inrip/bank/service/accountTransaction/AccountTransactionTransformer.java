package com.inrip.bank.service.accountTransaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

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

	public static List<AccountTransactionResponseDTO> pagedTransactionToResponseDTO(Page<AccountTransaction> pageTransactions) {
		List<AccountTransactionResponseDTO> listTransactionResponseDTO = new ArrayList<AccountTransactionResponseDTO>();		
		pageTransactions.forEach(item -> listTransactionResponseDTO.add(AccountTransactionTransformer.transactionToResponseDto(item)));
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
		AccountTransactionResponseDTO response = AccountTransactionResponseDTO.Builder.newInstance()
													.setReference(transaction.getReference())
													.setAccount_iban(transaction.getAccountiban())
													.setDate(transaction.getDate())
													.setAmount(transaction.getAmount())
													.setFee(transaction.getFee())
													.setDescription(transaction.getDescription())
													.build();
		return response;
	}
	

}

package com.inrip.bank.service.transaction;

import java.util.ArrayList;
import java.util.List;

import com.inrip.bank.dto.TransactionRequestDTO;
import com.inrip.bank.dto.TransactionResponseDTO;
import com.inrip.bank.model.Transaction;

/**
 * @author Enrique AC
 * 
 */
public class TransactionTransformer {

	
	public static List<TransactionResponseDTO> listTransactionToResponseDTO(List<Transaction> listTransactions) {
		List<TransactionResponseDTO> listTransactionResponseDTO = new ArrayList<TransactionResponseDTO>();		
		listTransactions.forEach(item -> listTransactionResponseDTO.add(TransactionTransformer.transactionToResponseDto(item)));
		return listTransactionResponseDTO;
	}


	public static Transaction transactionRequestDtoToTransaction(TransactionRequestDTO transactionRequest) {
		Transaction transaction = new Transaction();		
		transaction.setReference(transactionRequest.getReference());
		transaction.setAccountiban(transactionRequest.getAccount_iban());
		transaction.setDate( transactionRequest.getDate() );
		transaction.setAmount(transactionRequest.getAmount());
		transaction.setFee(transactionRequest.getFee());
		transaction.setDescription(transactionRequest.getDescription());
		return transaction;
	}

	public static TransactionResponseDTO transactionToResponseDto(Transaction transaction) {
		TransactionResponseDTO response = new TransactionResponseDTO();						
		response.setReference(transaction.getReference());
		response.setAccount_iban(transaction.getAccountiban());
		response.setDate(transaction.getDate());
		response.setAmount(transaction.getAmount());
		response.setFee(transaction.getFee());
		response.setDescription(transaction.getDescription());
		return response;
	}
	

}

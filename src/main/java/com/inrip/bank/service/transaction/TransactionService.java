package com.inrip.bank.service.transaction;

import java.util.List;
import java.util.Optional;

import com.inrip.bank.dto.TransactionRequestDTO;
import com.inrip.bank.dto.TransactionResponseDTO;
import com.inrip.bank.model.Transaction;

/**
 * @author Enrique AC
 *
 */
public interface TransactionService {
	public List<TransactionResponseDTO> getAllTransactions();
	public TransactionResponseDTO addTransaction(TransactionRequestDTO transactionRequestDTO)  throws Exception;	
	public List<TransactionResponseDTO> getTransactionByAccountIban(String account_iban, boolean descending_amount);
	public List<TransactionResponseDTO> getAllTransactionByReference(String strReference, boolean descending_amount);
	public Optional<Transaction> getTransactionByReference(String strReference);
}

package com.inrip.bank.service.accountTransaction;

import java.util.List;
import java.util.Optional;

import com.inrip.bank.dto.AccountTransactionRequestDTO;
import com.inrip.bank.dto.AccountTransactionResponseDTO;
import com.inrip.bank.model.AccountTransaction;

/**
 * @author Enrique AC
 *
 */
public interface AccountTransactionService {
	public List<AccountTransactionResponseDTO> getAllTransactions();
	public AccountTransactionResponseDTO addTransaction(AccountTransactionRequestDTO transactionRequestDTO)  throws Exception;	
	public List<AccountTransactionResponseDTO> getTransactionByAccountIban(String account_iban, int page, int limit, boolean descending_amount);
	public List<AccountTransactionResponseDTO> getAllTransactionByReference(String strReference, boolean descending_amount);
	public Optional<AccountTransaction> getTransactionByReference(String strReference);
}

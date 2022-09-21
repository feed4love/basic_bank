package com.inrip.bank.service.account;

import java.util.Optional;

import com.inrip.bank.dto.AccountRequestDTO;
import com.inrip.bank.dto.AccountResponseDTO;
import com.inrip.bank.dto.TransactionRequestDTO;

public interface AccountService {
    public Optional<AccountResponseDTO> findAccountByAccountIban(AccountRequestDTO accountRequestDTO);
    public Optional<AccountResponseDTO> addAccount(AccountRequestDTO accountRequestDTO);
    public AccountResponseDTO updateCreditByTransactionRequest(TransactionRequestDTO transactionRequestDTO);
}

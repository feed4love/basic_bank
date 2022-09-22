package com.inrip.bank.service.accountTransactionStatus;

import com.inrip.bank.controller.exceptions.SimpleBankHttpAcceptException;
import com.inrip.bank.dto.AccountTransactionStatusRequestDTO;
import com.inrip.bank.dto.AccountTransactionStatusResponseDTO;

/**
 * @author Enrique AC
 *
 */
public interface AccountTransactionStatusService {
	public AccountTransactionStatusResponseDTO getTransactionStatus(AccountTransactionStatusRequestDTO statusRequestDTO) ;
}

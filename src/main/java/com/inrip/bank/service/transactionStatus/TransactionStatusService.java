package com.inrip.bank.service.transactionStatus;

import com.inrip.bank.controller.exceptions.HttpAcceptException;
import com.inrip.bank.dto.StatusRequestDTO;
import com.inrip.bank.dto.StatusResponseDTO;

/**
 * @author Enrique AC
 *
 */
public interface TransactionStatusService {
	public StatusResponseDTO getTransactionStatus(StatusRequestDTO statusRequestDTO) ;
}

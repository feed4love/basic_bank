package com.inrip.bank.service.transactionStatus;

import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.inrip.bank.common.Utils;
import com.inrip.bank.dto.StatusRequestDTO;
import com.inrip.bank.dto.StatusResponseDTO;
import com.inrip.bank.model.Transaction;
import com.inrip.bank.service.transaction.TransactionService;
import com.inrip.bank.service.transaction.TransactionStatusLogicalValidator;

/**
 * @author Enrique AC
 *
 */
@Service
public class TransactionStatusServiceImpl implements TransactionStatusService {

	private static final Logger mLogger = LogManager.getLogger(TransactionStatusServiceImpl.class);

	/*@Autowired
	private TransactionRepository mTransactionRepository;*/

	@Autowired
	private TransactionService mTransactionService;

	@Value("${bank.basic.TRANSACTION_STATUS_TRUNCATE_DATES}")
	private boolean PARAM_TRANSACTION_STATUS_TRUNCATE_DATES;
			
	@Value("${bank.basic.DEBUG_DATA_ON_RESPONSES}")
	private boolean PARAM_DEBUG_DATA_ON_RESPONSES;

	@Override
	public StatusResponseDTO getTransactionStatus(StatusRequestDTO statusRequestDTO) {
		mLogger.info("Init - getTransactionStatus");
		
		//throws exception
		TransactionStatusLogicalValidator.validateStatusRequest(statusRequestDTO);	
		
		StatusResponseDTO statusResponseDTO = null;        
		Optional<Transaction> optTransaction = null;
		Transaction transactionDTO = null;
        
		Date todayDate = Utils.getToday(PARAM_TRANSACTION_STATUS_TRUNCATE_DATES);

        optTransaction = mTransactionService.getTransactionByReference(statusRequestDTO.getReference());

		//Bussines rule A
		statusResponseDTO = TransactionStatusLogicalValidator.doBusinessRule_A(statusRequestDTO, 
								optTransaction, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;
		transactionDTO = optTransaction.get();		

		//Bussines rule B
		statusResponseDTO = TransactionStatusLogicalValidator.doBusinessRule_B(statusRequestDTO, 
										transactionDTO, todayDate, 
										PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule C
		statusResponseDTO = TransactionStatusLogicalValidator.doBusinessRule_C(
					statusRequestDTO, transactionDTO, todayDate, 
					PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule D
		statusResponseDTO = TransactionStatusLogicalValidator.doBusinessRule_D(
					statusRequestDTO, transactionDTO, todayDate, 
					PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule E
		statusResponseDTO = TransactionStatusLogicalValidator.doBusinessRule_E(
					statusRequestDTO, transactionDTO, todayDate, PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, 
					PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule F
		statusResponseDTO = TransactionStatusLogicalValidator.doBusinessRule_F(
					statusRequestDTO, transactionDTO, todayDate, PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, 
					PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule G
		statusResponseDTO = TransactionStatusLogicalValidator.doBusinessRule_G(
					statusRequestDTO, transactionDTO, todayDate, 
					PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule H
		statusResponseDTO = TransactionStatusLogicalValidator.doBusinessRule_H(
						statusRequestDTO, transactionDTO, todayDate, 
						PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;

		if(statusResponseDTO==null){
			statusResponseDTO = new StatusResponseDTO(statusRequestDTO.getReference(), "UNKNOWN");
		}

		return statusResponseDTO;		
	}


}

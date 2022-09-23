package com.inrip.bank.service.accountTransactionStatus;

import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.server.ResponseStatusException;

import com.inrip.bank.common.SimpleBankUtils;
import com.inrip.bank.controller.exceptions.SimpleBankBadRequestException;
import com.inrip.bank.controller.exceptions.SimpleBankHttpAcceptException;
import com.inrip.bank.controller.exceptions.SimpleBankNotFoundException;
import com.inrip.bank.dto.AccountTransactionStatusRequestDTO;
import com.inrip.bank.dto.AccountTransactionStatusResponseDTO;
import com.inrip.bank.model.AccountTransaction;
import com.inrip.bank.service.accountTransaction.AccountTransactionService;
import com.inrip.bank.service.accountTransaction.AccountTransactionStatusLogicalValidator;

/**
 * @author Enrique AC
 *
 */
@Service
public class AccountTransactionStatusServiceImpl implements AccountTransactionStatusService {

	private static final Logger mLogger = LogManager.getLogger(AccountTransactionStatusServiceImpl.class);

	/*@Autowired
	private TransactionRepository mTransactionRepository;*/

	@Autowired
	private AccountTransactionService mTransactionService;

	
	@Value("${com.inrip.bank.param.uncomputable_status_transactions_returns_unknown}")
	private boolean PARAM_ACCEPT_UNKNOWN_TRANSACTION_STATUS;

	@Value("${com.inrip.bank.param.simple_dates_comparision}")
	private boolean PARAM_TRANSACTION_STATUS_TRUNCATE_DATES;
			
	@Value("${com.inrip.bank.param.debug.enabled}")
	private boolean PARAM_DEBUG_DATA_ON_RESPONSES;

	@Override
	public AccountTransactionStatusResponseDTO getTransactionStatus(AccountTransactionStatusRequestDTO statusRequestDTO) {
		mLogger.info("Init - getTransactionStatus");
		
		//throws exception
		// if field channel is null , then shall do something before continue
		// then, at this case and stage throws http202 ACCEPTED
		AccountTransactionStatusLogicalValidator.validateStatusRequest(statusRequestDTO);		
		
		AccountTransactionStatusResponseDTO statusResponseDTO = null;        
		Optional<AccountTransaction> optTransaction = null;
		AccountTransaction transactionDTO = null;
        
		Date todayDate = SimpleBankUtils.getToday(PARAM_TRANSACTION_STATUS_TRUNCATE_DATES);

        optTransaction = mTransactionService.getTransactionByReference(statusRequestDTO.getReference());

		//Bussines rule A, if match then return the response
		statusResponseDTO = AccountTransactionStatusLogicalValidator.doBusinessRule_A(statusRequestDTO, 
																optTransaction, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;
		transactionDTO = optTransaction.get();		

		//Bussines rule B, if match then return the response
		statusResponseDTO = AccountTransactionStatusLogicalValidator.doBusinessRule_B(statusRequestDTO, 
																transactionDTO, todayDate, 
																PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule C, if match then return the response
		statusResponseDTO = AccountTransactionStatusLogicalValidator.doBusinessRule_C(
					statusRequestDTO, transactionDTO, todayDate, 
					PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule D, if match then return the response
		statusResponseDTO = AccountTransactionStatusLogicalValidator.doBusinessRule_D(
					statusRequestDTO, transactionDTO, todayDate, 
					PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule E, if match then return the response
		statusResponseDTO = AccountTransactionStatusLogicalValidator.doBusinessRule_E(
					statusRequestDTO, transactionDTO, todayDate, PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, 
					PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule F, if match then return the response
		statusResponseDTO = AccountTransactionStatusLogicalValidator.doBusinessRule_F(
					statusRequestDTO, transactionDTO, todayDate, PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, 
					PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule G, if match then return the response
		statusResponseDTO = AccountTransactionStatusLogicalValidator.doBusinessRule_G(
					statusRequestDTO, transactionDTO, todayDate, 
					PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;		

        //Bussines rule H, if match then return the response
		statusResponseDTO = AccountTransactionStatusLogicalValidator.doBusinessRule_H(
						statusRequestDTO, transactionDTO, todayDate, 
						PARAM_TRANSACTION_STATUS_TRUNCATE_DATES, PARAM_DEBUG_DATA_ON_RESPONSES);
		if(statusResponseDTO!=null) return statusResponseDTO;

		if(!PARAM_ACCEPT_UNKNOWN_TRANSACTION_STATUS){
			throw new SimpleBankHttpAcceptException("NO_DATE_YET", "Until can't compute null dates");
		}
		
		statusResponseDTO = AccountTransactionStatusResponseDTO.Builder.newInstance()
								.setReference("UNKNOWN").build();
		//statusResponseDTO = new AccountTransactionStatusResponseDTO(statusRequestDTO.getReference(), "UNKNOWN");
		return statusResponseDTO;		
	}


}

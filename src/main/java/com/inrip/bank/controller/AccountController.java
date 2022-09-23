package com.inrip.bank.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inrip.bank.common.SimpleBankRequestMappings;
import com.inrip.bank.controller.handlers.SimpleBankHTTPResponseHandler;
import com.inrip.bank.dto.AccountTransactionStatusRequestDTO;
import com.inrip.bank.dto.AccountTransactionStatusResponseDTO;
import com.inrip.bank.dto.AccountTransactionRequestDTO;
import com.inrip.bank.dto.AccountTransactionResponseDTO;
import com.inrip.bank.service.account.AccountService;
import com.inrip.bank.service.accountTransaction.AccountTransactionService;
import com.inrip.bank.service.accountTransactionStatus.AccountTransactionStatusService;

/**
 * @author Enrique AC
 *	Main Rest Controller of Simple_Bank
 */

@RestController
@RequestMapping(SimpleBankRequestMappings.REQUEST_ACCOUNT_CONTEXT)
public class AccountController extends SimpleBankHTTPResponseHandler {

	private static final Logger mLogger = LogManager.getLogger(AccountController.class);

	@Autowired
	private AccountTransactionStatusService mTransactionStatusService;

	//@Autowired
	//private AccountService mAccountService;

	@Autowired
	private AccountTransactionService mTransactionService;

	@Value("${com.inrip.bank.param.alive_message}")
	private String PARAM_REST_RUNNING;

	@Value("${com.inrip.bank.param.debug.enabled}")
	private boolean PARAM_DEBUG_API_METHODS;

	/**
	 * check if the servicio is running on REQUEST_CONTEXT y CONTEXT_PATH
	 *
	 */
	@RequestMapping(value = SimpleBankRequestMappings.SERVICE_STATUS, method = RequestMethod.GET)
	public @ResponseBody String helloWorld() {
		return PARAM_REST_RUNNING;
	}

	/**
	 * List transaction filtering by account_iban and sort ascending or descending
	 *
	 */
	@RequestMapping(value = SimpleBankRequestMappings.SEARCH_BY_ACCOUNT_IBAN, method = RequestMethod.GET)
	public @ResponseBody List<AccountTransactionResponseDTO> searchTransactionByAccountIban(
							@PathVariable String account_iban,
							@RequestParam(value = "page", defaultValue = "0") int page,
							@RequestParam(value = "size", defaultValue = "5") int size,
							@RequestParam(name="descending_amount", required=false, defaultValue="false") boolean descending_amount
							) {		
		mLogger.info("Init - searchTransactionByAccountIban descending<" + descending_amount + ">");
		return mTransactionService.getTransactionByAccountIban(account_iban, page, size, descending_amount);
	}



	/**
	 * Add new transaction
	 * 
	 * 
	 */
	@RequestMapping(value = SimpleBankRequestMappings.ADD_TRANSACTION, method = RequestMethod.PUT)
	public @ResponseBody AccountTransactionResponseDTO addTransaction(@RequestBody AccountTransactionRequestDTO requestDTO) throws Exception {		
		mLogger.info("Init - addTransaction <" + requestDTO.toString() + ">");
		AccountTransactionResponseDTO transactionResponseDTO  = mTransactionService.addTransaction(requestDTO);
		return transactionResponseDTO;
	}

	/**
	 * Status transaction
	 * 
	 *  Filter by account_iban
	 *  Sort by amount (ascending/descending)
	 */
	@RequestMapping(value = SimpleBankRequestMappings.TRANSACTION_STATUS, method = RequestMethod.GET, produces="application/json")	
	@ResponseBody
	public AccountTransactionStatusResponseDTO transactionStatus(@RequestBody AccountTransactionStatusRequestDTO statusRequestDTO) {	
		AccountTransactionStatusResponseDTO resp = null;
	
		mLogger.info("Init - transactionStatus <" + statusRequestDTO.toString() + ">");
		resp = mTransactionStatusService.getTransactionStatus(statusRequestDTO);	
		
		return resp;
	}



}

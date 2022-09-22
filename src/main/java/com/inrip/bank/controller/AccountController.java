package com.inrip.bank.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inrip.bank.common.SimpleBankRequestMappings;
import com.inrip.bank.controller.exceptions.SimpleBankHttpAcceptException;
import com.inrip.bank.controller.handlers.SimpleBankHTTPResponseHandler;
import com.inrip.bank.dto.AccountRequestDTO;
import com.inrip.bank.dto.AccountResponseDTO;
import com.inrip.bank.dto.AccountTransactionStatusRequestDTO;
import com.inrip.bank.dto.AccountTransactionStatusResponseDTO;
import com.inrip.bank.dto.AccountTransactionRequestDTO;
import com.inrip.bank.dto.AccountTransactionResponseDTO;
import com.inrip.bank.service.account.AccountService;
import com.inrip.bank.service.accountTransaction.AccountTransactionService;
import com.inrip.bank.service.accountTransactionStatus.AccountTransactionStatusService;

import javassist.NotFoundException;

/**
 * @author Enrique AC
 *	Main Rest Controller of Simple_Bank
 */

@RestController
@RequestMapping(SimpleBankRequestMappings.REQUEST_CONTEXT)
public class AccountController extends SimpleBankHTTPResponseHandler {

	private static final Logger mLogger = LogManager.getLogger(AccountController.class);

	@Autowired
	private AccountTransactionService mTransactionService;

	@Autowired
	private AccountTransactionStatusService mTransactionStatusService;

	@Autowired
	private AccountService mAccountService;

	@Value("${bank.basic.message.alive}")
	private String PARAM_REST_RUNNING;

	@Value("${bank.basic.DEBUG_API_METHODS}")
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
	 * extra: list all transacciones (4debug)
	 *
	 */
	@RequestMapping(value = SimpleBankRequestMappings.LIST_ALL, method = RequestMethod.GET)
	public @ResponseBody List<AccountTransactionResponseDTO> listAllTransactions() {
		if(!PARAM_DEBUG_API_METHODS)
			return null;

		List<AccountTransactionResponseDTO> listTransactionResponseDTO = new ArrayList<AccountTransactionResponseDTO>();
		mLogger.info("Init - listAllTransactions");
		listTransactionResponseDTO = mTransactionService.getAllTransactions();
		return  listTransactionResponseDTO;
	}

	/**
	 * List transaction filtering by account_iban and sort ascending or descending
	 *
	 */
	@RequestMapping(value = SimpleBankRequestMappings.SEARCH_BY_ACCOUNT_IBAN, method = RequestMethod.GET)
	public @ResponseBody List<AccountTransactionResponseDTO> searchTransactionByAccountIban(
							@PathVariable String account_iban,
							@RequestParam(name="descending_amount", required=false, defaultValue="false") boolean descending_amount
							) {		
		mLogger.info("Init - searchTransactionByAccountIban descending<" + descending_amount + ">");
		return mTransactionService.getTransactionByAccountIban(account_iban, descending_amount);
	}

	/**
	 * extra: list transactions filtering by reference (4debug)
	 * @throws NotFoundException
	 *
	 */
	@RequestMapping(value = SimpleBankRequestMappings.SEARCH_BY_REFERENCE, method = RequestMethod.GET)
	public @ResponseBody List<AccountTransactionResponseDTO> searchTransactionByReference(
							@PathVariable String reference,
							@RequestParam(name="descending_amount", required=false, defaultValue="false") boolean descending_amount
							) throws NotFoundException {		
		if(!PARAM_DEBUG_API_METHODS)
			return null;
					
		List<AccountTransactionResponseDTO> resp = new ArrayList<AccountTransactionResponseDTO>();
		mLogger.info("Init - searchTransactionByReference descending<" + descending_amount + ">");
		resp = mTransactionService.getAllTransactionByReference(reference, descending_amount);
		return resp;
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

	/**
	 *  Obtain an Account 
	 *  Filter by account_iban
	 */
	@RequestMapping(value = SimpleBankRequestMappings.SEARCH_ACCOUNT_BY_IBAN, 
	                method = RequestMethod.GET, 
					produces="application/json")
	@ResponseBody
	public AccountResponseDTO getAccountByAccountIban(@PathVariable String account_iban) {	
		AccountResponseDTO accountResponseDTO = null;	
		Optional<AccountResponseDTO> optAccountResponseDTO = null;	

		mLogger.info("Init - getAccountByAccountIban <" + account_iban + ">");
		
		AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
		accountRequestDTO.setAccountiban(account_iban);

		optAccountResponseDTO = mAccountService.findAccountByAccountIban(accountRequestDTO);
		if(optAccountResponseDTO.isPresent())
			accountResponseDTO = optAccountResponseDTO.get();
		
		return accountResponseDTO;
	}



}

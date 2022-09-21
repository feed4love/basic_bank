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

import com.inrip.bank.common.RequestMappings;
import com.inrip.bank.controller.handlers.HTTPResponseHandler;
import com.inrip.bank.dto.StatusRequestDTO;
import com.inrip.bank.dto.StatusResponseDTO;
import com.inrip.bank.dto.TransactionRequestDTO;
import com.inrip.bank.dto.TransactionResponseDTO;
import com.inrip.bank.service.transaction.TransactionService;
import com.inrip.bank.service.transactionStatus.TransactionStatusService;

/**
 * @author Enrique AC
 *
 */

@RestController
@RequestMapping(RequestMappings.REQUEST_CONTEXT)
public class TransactionController extends HTTPResponseHandler {

	private static final Logger mLogger = LogManager.getLogger(TransactionController.class);

	@Autowired
	private TransactionService mTransactionService;

	@Autowired
	private TransactionStatusService mTransactionStatusService;

	@Value("${bank.basic.message.alive}")
	private String REST_RUNNING;

	/**
	 * Comprobar que el servicio esta activo en el REQUEST_CONTEXT y CONTEXT_PATH
	 *
	 */
	@RequestMapping(value = RequestMappings.SERVICE_STATUS, method = RequestMethod.GET)
	public @ResponseBody String helloWorld() {
		return REST_RUNNING;
	}

	/**
	 * Listar todas las transacciones
	 *
	 */
	@RequestMapping(value = RequestMappings.LIST_ALL, method = RequestMethod.GET)
	public @ResponseBody List<TransactionResponseDTO> listAllTransactions() {
		mLogger.info("Init - listAllTransactions");
		return mTransactionService.getAllTransactions();
	}

	/**
	 * List transaction filtering by account_iban and sort ascending or descending
	 *
	 */
	@RequestMapping(value = RequestMappings.SEARCH_BY_ACCOUNT_IBAN, method = RequestMethod.GET)
	public @ResponseBody List<TransactionResponseDTO> searchTransactionByAccountIban(
							@PathVariable    			
							String account_iban,
							@RequestParam(name="descending_amount", required=false, defaultValue="false") boolean descending_amount
							) {		
		mLogger.info("Init - searchTransactionByAccountIban descending<" + descending_amount + ">");
		return mTransactionService.getTransactionByAccountIban(account_iban, descending_amount);
	}

	/**
	 * Extra: list transactions filtering by reference
	 *
	 */
	@RequestMapping(value = RequestMappings.SEARCH_BY_REFERENCE, method = RequestMethod.GET)
	public @ResponseBody List<TransactionResponseDTO> searchTransactionByReference(
							@PathVariable String reference,
							@RequestParam(name="descending_amount", required=false, defaultValue="false") boolean descending_amount
							) {		
		mLogger.info("Init - searchTransactionByReference descending<" + descending_amount + ">");
		List<TransactionResponseDTO> resp = mTransactionService.getAllTransactionByReference(reference, descending_amount);
		return resp;
	}

	/**
	 * Add new transaction
	 * 
	 * 
	 */
	@RequestMapping(value = RequestMappings.ADD_TRANSACTION, method = RequestMethod.POST)
	public @ResponseBody TransactionResponseDTO addTransaction(@RequestBody TransactionRequestDTO requestDTO) throws Exception {		
		mLogger.info("Init - addTransaction <" + requestDTO.toString() + ">");
		TransactionResponseDTO transactionResponseDTO  = mTransactionService.addTransaction(requestDTO);
		return transactionResponseDTO;
	}

	/**
	 * Status transaction
	 * 
	 *  Filter by account_iban
	 *  Sort by amount (ascending/descending)
	 */
	@RequestMapping(value = RequestMappings.TRANSACTION_STATUS, method = RequestMethod.GET, produces="application/json")	
	@ResponseBody
	public StatusResponseDTO transactionStatus(@RequestBody StatusRequestDTO statusRequestDTO) {		
		mLogger.info("Init - transactionStatus <" + statusRequestDTO.toString() + ">");
		StatusResponseDTO resp = mTransactionStatusService.getTransactionStatus(statusRequestDTO);
		return resp;
	}



}

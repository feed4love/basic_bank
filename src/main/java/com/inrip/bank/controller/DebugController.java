package com.inrip.bank.controller;

import com.inrip.bank.common.SimpleBankRequestMappings;
import com.inrip.bank.dto.AccountTransactionResponseDTO;
import com.inrip.bank.controller.handlers.SimpleBankHTTPResponseHandler;
import com.inrip.bank.dto.AccountTransactionStatusRequestDTO;
import com.inrip.bank.dto.AccountTransactionStatusResponseDTO;
import com.inrip.bank.dto.AccountRequestDTO;
import com.inrip.bank.dto.AccountResponseDTO;
import com.inrip.bank.dto.AccountTransactionRequestDTO;
import com.inrip.bank.service.account.AccountService;
import com.inrip.bank.service.accountTransaction.AccountTransactionService;
import com.inrip.bank.service.accountTransactionStatus.AccountTransactionStatusService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(
        prefix="com.inrip.bank.param.debug",
        name="enabled",
        havingValue="true")
@RequestMapping(SimpleBankRequestMappings.REQUEST_DEBUG_CONTEXT)
@RestController
public class DebugController  extends SimpleBankHTTPResponseHandler {
    
    private static final Logger mLogger = LogManager.getLogger(DebugController.class);

    @Autowired
    private AccountService mAccountService;

    @Autowired
    private AccountTransactionService mTransactionService;

	/**
	 * extra: list all transacciones (4debug)
	 *
	 */
	@RequestMapping(value = SimpleBankRequestMappings.LIST_ALL, method = RequestMethod.GET)
	public @ResponseBody List<AccountTransactionResponseDTO> listAllTransactions() {
		List<AccountTransactionResponseDTO> listTransactionResponseDTO = new ArrayList<AccountTransactionResponseDTO>();
		mLogger.info("Init - listAllTransactions");
		listTransactionResponseDTO = mTransactionService.getAllTransactions();
		return  listTransactionResponseDTO;
	}

	/**
	 * extra: list transactions filtering by reference (4debug)
	 *
	 */
	@RequestMapping(value = SimpleBankRequestMappings.SEARCH_BY_REFERENCE, method = RequestMethod.GET)
	public @ResponseBody List<AccountTransactionResponseDTO> searchTransactionByReference(
							@PathVariable String reference,
							@RequestParam(name="descending_amount", required=false, defaultValue="false") boolean descending_amount
							) {		
		List<AccountTransactionResponseDTO> resp = new ArrayList<AccountTransactionResponseDTO>();
		mLogger.info("Init - searchTransactionByReference descending<" + descending_amount + ">");
		resp = mTransactionService.getAllTransactionByReference(reference, descending_amount);
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
		
		AccountRequestDTO accountRequestDTO = AccountRequestDTO.Builder.newInstance()
													.setAccountiban(account_iban)
													.build();

		optAccountResponseDTO = mAccountService.findAccountByAccountIban(accountRequestDTO);
		if(optAccountResponseDTO.isPresent())
			accountResponseDTO = optAccountResponseDTO.get();
		
		return accountResponseDTO;
	}


}

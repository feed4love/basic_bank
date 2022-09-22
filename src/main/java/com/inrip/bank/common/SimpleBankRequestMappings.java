package com.inrip.bank.common;

/**
 * @author Enrique AC
 *
 */
public interface SimpleBankRequestMappings {	
	public static String REQUEST_CONTEXT = "/api/account";
	public static String SERVICE_STATUS = "/";	
	public static String SEARCH_BY_ACCOUNT_IBAN = "/transaction/iban/{account_iban}";	
	public static String ADD_TRANSACTION = "/transaction/add";
	public static String TRANSACTION_STATUS = "/transaction/status";

	//solo debug
	public static String LIST_ALL = "/transaction/all";	
	public static String SEARCH_BY_REFERENCE = "/transaction/reference/{reference}";
	public static String SEARCH_ACCOUNT_BY_IBAN = "/{account_iban}";
}

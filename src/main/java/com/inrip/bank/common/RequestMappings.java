package com.inrip.bank.common;

/**
 * @author Enrique AC
 *
 */
public interface RequestMappings {	
	public static String REQUEST_CONTEXT = "/api/transaction";	
	public static String SERVICE_STATUS = "/";	
	public static String SEARCH_BY_ACCOUNT_IBAN = "/iban/{account_iban}";
	public static String SEARCH_BY_REFERENCE = "/reference/{reference}";
	public static String ADD_TRANSACTION = "/add";
	public static String TRANSACTION_STATUS = "/status";

	//solo debug
	public static String LIST_ALL = "/all";	
}

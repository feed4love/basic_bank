package com.inrip.bank.common;

/**
 * @author Enrique AC
 *
 */
public interface SimpleBankRequestMappings {

	//parent path mounted after the servlet context
	public static String REQUEST_ACCOUNT_CONTEXT = "";
	public static String REQUEST_DEBUG_CONTEXT   = "/debug";

	//path hello world
	public static String SERVICE_STATUS = "/";

	//path transacciones
	public static String SEARCH_BY_ACCOUNT_IBAN = "/account/transaction/iban/{account_iban}";	
	public static String ADD_TRANSACTION        = "/account/transaction/add";
	public static String TRANSACTION_STATUS     = "/account/transaction/status";

	//solo debug
	public static String SEARCH_ACCOUNT_BY_IBAN = "/account/{account_iban}";
	public static String LIST_ALL               = "/account/transaction/all";	
	public static String SEARCH_BY_REFERENCE    = "/account/transaction/reference/{reference}";
	
}

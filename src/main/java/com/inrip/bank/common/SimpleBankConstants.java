package com.inrip.bank.common;

/**
 *
 */
public interface SimpleBankConstants {
	
	public static String PARAMTYPE_HEADER = "header";
	public static String HTTP_METHOD_GET = "GET";
	public static String HTTP_METHOD_POST = "POST";
	public static String HTTP_METHOD_PUT = "PUT";
	public static String HTTP_METHOD_DELETE = "DELETE";
	public static int HTTP_STATUS_SUCCESS_STATUS_CODE = 200;	
	public static String HTTP_STATUS_SUCCESS_STATUS = "SUCCESS";	
	public static String HTTP_STATUS_SUCCESS_STATUS_DESCRIPTION = "Success";
	public static int HTTP_STATUS_ACCEPT_STATUS_CODE = 202;
	public static String HTTP_STATUS_ACCEPT_STATUS = "ACCEPT";	
	public static String HTTP_STATUS_ACCEPT_STATUS_DESCRIPTION = "Accept";
	public static int HTTP_STATUS_BAD_REQUEST_STATUS_CODE = 400;
	public static String HTTP_STATUS_BAD_REQUEST_STATUS = "BAD_REQUEST";
	public static String HTTP_STATUS_BAD_REQUEST_STATUS_DESCRIPTION = "Bad Request";
	public static int HTTP_STATUS_UNAUTHORIZED_STATUS_CODE = 401;
	public static String HTTP_STATUS_UNAUTHORIZED_STATUS_DESCRIPTION = "Unauthorized";
	public static int HTTP_STATUS_FORBIDDEN_STATUS_CODE = 403;
	public static String HTTP_STATUS_FORBIDDEN_STATUS_DESCRIPTION = "Forbidden";
	public static int HTTP_STATUS_NOT_FOUND_STATUS_CODE = 404;
	public static String HTTP_STATUS_NOT_FOUND_STATUS = "NOT_FOUND";
	public static String HTTP_STATUS_NOT_FOUND_STATUS_DESCRIPTION = "Not Found";
	public static int HTTP_STATUS_FAILURE_STATUS_CODE = 500;
	public static String HTTP_STATUS_FAILURE_STATUS_DESCRIPTION = "Failure";
	public static String CONTENT_TYPE = "Content-Type";
	public static String DATE_FORMAT = "yyyy-MM-dd";
	public static String DATE_FORMAT_REGX = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";


	public enum TRANSACTION_STATUS  {
        INVALID("INVALID"),
        SETTLED("SETTLED"),
        PENDING("PENDING"),
        FUTURE("FUTURE");
        private String status;    
        TRANSACTION_STATUS(String status) {
            this.status = status;
        }    
        public String get() {
            return status;
        }
    }

	public class JWTConstants {
		public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
		public static final String SIGNING_KEY = "55a0653280be438084b6a4151daf26a1d274e76a4b0d408db86fa9aa51e616bf55a0653280be438084b6a4151daf26a1d274e76a4b0d408db86fa9aa51e616bf";
		public static final String TOKEN_PREFIX = "Bearer ";
		public static final String HEADER_STRING = "Authorization";
	}	

	
}

package com.inrip.bank.controller.exceptions;

/**
 *
 */
public class SimpleBankNotFoundException extends SimpleBankHTTPException {

	private static final long serialVersionUID = 1L;

	public SimpleBankNotFoundException(String statusMessage, String endUserMessage, String externalMessage) {
		super(statusMessage, endUserMessage, externalMessage);
	}

	public SimpleBankNotFoundException(String statusMessage, String endUserMessage) {
		super(statusMessage, endUserMessage);
	}

	public SimpleBankNotFoundException(String statusMessage) {
		super(statusMessage);
	}

}

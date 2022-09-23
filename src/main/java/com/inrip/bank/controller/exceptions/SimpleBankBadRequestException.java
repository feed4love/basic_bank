package com.inrip.bank.controller.exceptions;

public class SimpleBankBadRequestException extends SimpleBankHTTPException {

	private static final long serialVersionUID = 1L;

	public SimpleBankBadRequestException(String statusMessage, String endUserMessage, String externalMessage) {
		super(statusMessage, endUserMessage, externalMessage);
	}

	public SimpleBankBadRequestException(String statusMessage, String endUserMessage) {
		super(statusMessage, endUserMessage);
	}

	public SimpleBankBadRequestException(String statusMessage) {
		super(statusMessage);
	}

}
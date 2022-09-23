package com.inrip.bank.controller.exceptions;

public class SimpleBank extends SimpleBankHTTPException {

	private static final long serialVersionUID = 1L;

	public SimpleBank(String statusMessage, String endUserMessage, String externalMessage) {
		super(statusMessage, endUserMessage, externalMessage);
	}

	public SimpleBank(String statusMessage, String endUserMessage) {
		super(statusMessage, endUserMessage);
	}

	public SimpleBank(String statusMessage) {
		super(statusMessage);
	}

}
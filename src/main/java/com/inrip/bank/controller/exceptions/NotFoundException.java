package com.inrip.bank.controller.exceptions;

/**
 *
 */
public class NotFoundException extends HTTPException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String statusMessage, String endUserMessage, String externalMessage) {
		super(statusMessage, endUserMessage, externalMessage);
	}

	public NotFoundException(String statusMessage, String endUserMessage) {
		super(statusMessage, endUserMessage);
	}

	public NotFoundException(String statusMessage) {
		super(statusMessage);
	}

}

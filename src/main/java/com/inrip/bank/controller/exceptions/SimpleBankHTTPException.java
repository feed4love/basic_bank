package com.inrip.bank.controller.exceptions;

/**
 *
 */
public class SimpleBankHTTPException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String statusMessage;

	private String externalMessage;

	private String endUserMessage;

	private boolean logException = true;

	public SimpleBankHTTPException(String statusMessage) {
		super();
		this.statusMessage = statusMessage;
	}

	public SimpleBankHTTPException(String statusMessage, String endUserMessage) {
		super(endUserMessage);
		this.statusMessage = statusMessage;
		this.endUserMessage = endUserMessage;				
	}

	public SimpleBankHTTPException(String statusMessage, String endUserMessage, String externalMessage) {
		super();
		this.statusMessage = statusMessage;
		this.endUserMessage = endUserMessage;
		this.externalMessage = externalMessage;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getExternalMessage() {
		return externalMessage;
	}

	public void setExternalMessage(String externalMessage) {
		this.externalMessage = externalMessage;
	}

	public boolean isLogException() {
		return logException;
	}

	public void setLogException(boolean logException) {
		this.logException = logException;
	}

	public String getEndUserMessage() {
		return endUserMessage;
	}
}

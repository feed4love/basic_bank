package com.inrip.bank.dto;

/**
 * @author Enrique AC
 * 
	Payload:
	{
	"reference":"12345A",
	"channel":"CLIENT"
	}
 *
 */
public class AccountTransactionStatusRequestDTO {

	private String reference;
	private String channel;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	

}

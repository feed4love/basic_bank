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

	private final String reference;
	private final String channel;

	public AccountTransactionStatusRequestDTO(String reference, String channel) {
		this.reference = reference;
		this.channel = channel;
	}
	public AccountTransactionStatusRequestDTO(Builder builder) {		
        this.reference  = builder.reference;        
        this.channel    = builder.channel;
    }

	public static class Builder {
		private String reference;
		private String channel;
	
		private Builder() {}
		public static Builder newInstance() {
            return new Builder();
        }		
		public AccountTransactionStatusRequestDTO build() {
			return new AccountTransactionStatusRequestDTO(this);
		}
		public Builder setReference(String reference) {
			this.reference = reference;
			return this;
		}
		public Builder setChannel(String channel) {
			this.channel = channel;
			return this;
		}
	}
	public String getReference() {
		return reference;
	}
	public String getChannel() {
		return channel;
	}
	
	@Override
	public String toString() {
		return "AccountTransactionStatusRequestDTO [channel=" + channel + ", reference=" + reference + "]";
	}

}

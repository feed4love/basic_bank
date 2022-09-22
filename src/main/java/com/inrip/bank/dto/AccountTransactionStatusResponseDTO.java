package com.inrip.bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Enrique AC
 * 
	Status:
	{
		"reference":"12345A",
		"status":"PENDING",
		"amount":193.38,
		"fee":3.18
	}
	reference: The transaction reference number
	status: The status of the transaction. It can be any of these values: PENDING, SETTLED, FUTURE, INVALID
	amount: the amount of the transaction
	fee: The fee applied to the transactio
 *
 */
@JsonInclude(Include.NON_NULL)
public class AccountTransactionStatusResponseDTO {
	private final String reference;
	private final String status;
	private final Double amount;
	private final Double fee;
	private final String debug;


/*
 * 
	public AccountTransactionStatusResponseDTO(String reference, String status) {
		this.reference = reference;
		this.status = status;
	}

	public AccountTransactionStatusResponseDTO(String reference, String status, Double amount) {
		this.reference = reference;
		this.status = status;
		this.amount = amount;
	}

	public AccountTransactionStatusResponseDTO(String reference, String status, Double amount, Double fee) {
		this.reference = reference;
		this.status = status;
		this.amount = amount;
		this.fee = fee;
	}
	
 * 
 */
	
	public AccountTransactionStatusResponseDTO(Builder builder) {		
        this.reference = builder.reference;
		this.status    = builder.status;
		this.amount    = builder.amount;
		this.fee       = builder.fee;
		this.debug     = builder.debug;
	}


	public static class Builder {
		private String reference;
		private String status;
		private Double amount;
		private Double fee;
		private String debug;
	
		private Builder() {}
		public static Builder newInstance() {
            return new Builder();
        }		
		public AccountTransactionStatusResponseDTO build() {
			return new AccountTransactionStatusResponseDTO(this);
		}
		public Builder setReference(String reference) {
			this.reference = reference;
			return this;
		}
		public Builder setStatus(String status) {
			this.status = status;
			return this;
		}
		public Builder setAmount(Double amount) {
			this.amount = amount;
			return this;
		}
		public Builder setFee(Double fee) {
			this.fee = fee;
			return this;
		}
		public Builder setDebug(String debug) {
			this.debug = debug;
			return this;
		}
	}



	public String getReference() {
		return reference;
	}

	public String getStatus() {
		return status;
	}

	public Double getAmount() {
		return amount;
	}

	public Double getFee() {
		return fee;
	}

	public String getDebug() {
		return debug;
	}

}

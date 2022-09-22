package com.inrip.bank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Date;

/**
 * @author Enrique AC
 *
 */
@JsonInclude(Include.NON_NULL)
public class AccountTransactionResponseDTO {		
	private final String reference;
	private final String account_iban;
	private final Date date;	
	private final Double amount;
	private final Double fee;
	private final String description;

	public AccountTransactionResponseDTO(Builder builder) {		
        this.account_iban = builder.account_iban;        
        this.reference    = builder.reference;
        this.date         = builder.date;
        this.amount       = builder.amount;
        this.fee          = builder.fee;
        this.description  = builder.description;
    }

	public static class Builder {
		private String reference;
		private String account_iban;
		private Date date;	
		private Double amount;
		private Double fee;
		private String description;

		private Builder() {}
		public static Builder newInstance() {
            return new Builder();
        }		
		public AccountTransactionResponseDTO build() {
			return new AccountTransactionResponseDTO(this);
		}

		public Builder setReference(String reference) {
			this.reference = reference;
			return this;
		}
		public Builder setAccount_iban(String account_iban) {
			this.account_iban = account_iban;
			return this;
		}
		public Builder setDate(Date date) {
			this.date = date;
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
		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}
	}

	public String getReference() {
		return reference;
	}

	public String getAccount_iban() {
		return account_iban;
	}

	public Date getDate() {
		return date;
	}

	public Double getAmount() {
		return amount;
	}

	public Double getFee() {
		return fee;
	}

	public String getDescription() {
		return description;
	}


}

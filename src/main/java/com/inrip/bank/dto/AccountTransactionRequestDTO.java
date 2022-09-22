package com.inrip.bank.dto;

import java.util.Date;

/**
 * @author Enrique AC
 *
 */
public class AccountTransactionRequestDTO {

	private final String uid;
	private final String reference;
	private final String account_iban;
	private final Date date;	
	private final Double amount;
	private final Double fee;
	private final String description;

	public AccountTransactionRequestDTO(String uid, String reference, String account_iban, Date date, Double amount,
		Double fee, String description) {
			this.uid = uid;
			this.reference = reference;
			this.account_iban = account_iban;
			this.date = date;
			this.amount = amount;
			this.fee = fee;
			this.description = description;
	}


	public AccountTransactionRequestDTO(Builder builder) {
        this.account_iban = builder.account_iban;
        this.uid          = builder.uid;
        this.reference    = builder.reference;
        this.date         = builder.date;
        this.amount       = builder.amount;
        this.fee          = builder.fee;
        this.description  = builder.description;
    }

	public static class Builder {
		private String uid;
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
		public AccountTransactionRequestDTO build() {
			return new AccountTransactionRequestDTO(this);
		}
		public Builder setUid(String uid) {
			this.uid = uid;
			return this;
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

	public String getUid() {
		return uid;
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

	@Override
	public String toString() {
		return "TransactionRequestDTO [date=" + date + ", description=" + description + ", uid=" + uid + "]";
	}

}

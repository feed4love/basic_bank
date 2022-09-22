package com.inrip.bank.dto;

import com.inrip.bank.model.AccountTransaction;

/**
 * @author Enrique AC
 *
 */
public class AccountRequestDTO {
	
	private final String account_iban;
	private final Double credit;
	
    public AccountRequestDTO(Builder builder) {
        this.account_iban = builder.account_iban;
        this.credit      = builder.credit;
    }

    public static class Builder {
		private String account_iban;
		private Double credit;		
		
		private Builder() {}
		public static Builder newInstance() {
            return new Builder();
        }
		public Builder setAccountiban(String account_iban) {
			this.account_iban = account_iban;
			return this;
		}
		public Builder setCredit(Double credit) {
			this.credit = credit;
			return this;
		}
		public AccountRequestDTO build() {
			return new AccountRequestDTO(this);
		}
		public AccountTransaction buildTransaction() {
			AccountTransaction mAccountTransaction = new AccountTransaction ();
			mAccountTransaction.setAccountiban(this.account_iban);
			return mAccountTransaction;
		}
	}
	
	public String getAccountiban() {
		return account_iban;
	}

	public Double getCredit() {
		return credit;
	}

	@Override
	public String toString() {
		return "AccountRequestDTO [account_iban=" + account_iban + ", credit=" + credit + "]";
	}

}

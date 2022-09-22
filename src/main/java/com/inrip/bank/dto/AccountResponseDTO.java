package com.inrip.bank.dto;

/**
 * @author Enrique AC
 *
 */
public class AccountResponseDTO {
	
	private final String uid;
	private final String accountiban;
	private final Double credit;

    public AccountResponseDTO(Builder builder) {
        this.uid         = builder.uid;
        this.accountiban = builder.accountiban;
		this.credit      = builder.credit;
    }

    public static class Builder {
		private String uid;
		private String accountiban;
		private Double credit;
			
		private Builder() {}
		public static Builder newInstance() {
            return new Builder();
        }
		public Builder setUid(String uid) {
			this.uid = uid;
			return this;
		}
		public Builder setAccountiban(String account_iban) {
			this.accountiban = account_iban;
			return this;
		}
		public Builder setCredit(Double credit) {
			this.credit = credit;
			return this;
		}
		public AccountResponseDTO build() {
			return new AccountResponseDTO(this);
		}

	}
	
	public String getUid() {
		return uid;
	}

	public String getAccountiban() {
		return accountiban;
	}

	public Double getCredit() {
		return credit;
	}

	@Override
	public String toString() {
		return "AccountResponseDTO [accountiban=" + accountiban + ", credit=" + credit + ", uid=" + uid + "]";
	}

}

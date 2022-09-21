package com.inrip.bank.dto;

/**
 * @author Enrique AC
 *
 */
public class AccountResponseDTO {
	
	private String uid;

	private String accountiban;

	private Double credit;
	
	
	public AccountResponseDTO(){		
	}	

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAccountiban() {
		return accountiban;
	}

	public void setAccountiban(String accountiban) {
		this.accountiban = accountiban;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	@Override
	public String toString() {
		return "AccountResponseDTO [accountiban=" + accountiban + ", credit=***, uid=" + uid + "]";
	}


}

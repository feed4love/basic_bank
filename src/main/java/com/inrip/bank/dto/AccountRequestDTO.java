package com.inrip.bank.dto;

/**
 * @author Enrique AC
 *
 */
public class AccountRequestDTO {
	
	private String accountiban;
	private Double credit;
	
	public AccountRequestDTO(){		
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



}

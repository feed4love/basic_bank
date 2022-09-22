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

	private String reference;
	private String status;
	private Double amount;
	private Double fee;
	private String debug;

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

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

}

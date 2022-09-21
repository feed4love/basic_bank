package com.inrip.bank.model;

import java.util.Date;

/* para H2 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

// para mongo
/*import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;-*

/**
 * @author Enrique AC
  * Payload:
		{
		"reference":"12345A"
		"account_iban":"ES9820385778983000760236",
		"date":"2019-07-16T16:55:42.000Z",
		"amount":193.38,
		"fee":3.18,
		"description":"Restaurant payment"
		}
 *
 */

@Entity
@Table
//@Document(collection = "transaction")
public class Transaction {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String uid;

	//@TextIndexed
	private String reference;

	private String accountiban;
	
	private Date date;	

	private Double amount;

	private Double fee;

	private String description;

	public Transaction(){		
	}

	public Transaction(String reference){
		this.reference = reference;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getAccountiban() {
		return accountiban;
	}

	public void setAccountiban(String accountiban) {
		this.accountiban = accountiban;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

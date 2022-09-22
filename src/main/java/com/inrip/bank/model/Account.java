package com.inrip.bank.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
/* para H2 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

// para mongo
/*import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;*/

/**
 * @author Enrique AC
 *    Account class to support operations of debit or credit
 *
 */
@Entity(name = "Account")
@Table (name = "simple_bank_account")
//@Document(collection = "account")
public class Account {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	//the external id to share
	private String uid;

	//@TextIndexed
	private String accountiban;

	private Double credit;

	//relation with transaction many to one
	@OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    List<Transaction> transactions = new ArrayList<Transaction>();
	
	public Account(){		
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


}

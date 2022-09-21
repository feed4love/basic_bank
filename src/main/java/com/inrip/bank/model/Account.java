package com.inrip.bank.model;

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
import org.springframework.data.mongodb.core.mapping.Document;*/

/**
 * @author Enrique AC
 *    Account class to support operations of debit or credit
 *
 */
@Entity
@Table
//@Document(collection = "account")
public class Account {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String uid;

	//@TextIndexed
	private String accountiban;

	private Double credit;
	
	
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

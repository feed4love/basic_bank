package com.inrip.bank.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

/* para H2 */
import org.springframework.data.jpa.repository.JpaRepository;

/* para mongo */
/*import org.springframework.data.mongodb.repository.MongoRepository;
*/

import com.inrip.bank.model.Transaction;

/**
 * @author Enrique AC
 *
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//public interface TransactionRepository<T, I extends Serializable> extends MongoRepository<Transaction, String> {        
    Optional<Transaction> findByReference(String reference);
    List<Transaction> findByReference(String reference, Sort sort);
    List<Transaction> findAllByAccountiban(String accountiban, Sort sort);       
}

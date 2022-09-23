package com.inrip.bank.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

/* para H2 */
import org.springframework.data.jpa.repository.JpaRepository;

/* para mongo */
/*import org.springframework.data.mongodb.repository.MongoRepository;
*/

import com.inrip.bank.model.AccountTransaction;

/**
 * @author Enrique AC
 *
 */

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
//public interface TransactionRepository<T, I extends Serializable> extends MongoRepository<Transaction, String> {        
    Optional<AccountTransaction> findByReference(String reference);
    List<AccountTransaction> findByReference(String reference, Sort sort);
    List<AccountTransaction> findAllByAccountiban(String accountiban, Sort sort);
    Page<AccountTransaction> findAllByAccountiban(String accountiban, Pageable  pageRequest);
}

package com.inrip.bank.repository;

import java.io.Serializable;
import java.util.Optional;

/* para H2 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/* para mongo */
//import org.springframework.data.mongodb.repository.MongoRepository;

import com.inrip.bank.model.Account;

/**
 * @author Enrique AC
 *   repositorio de cuenta para acceder al credito
 *
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//public interface AccountRepository<T, I extends Serializable> extends MongoRepository<Account, String> {    
    Optional<Account> findOneByAccountiban(String accountiban);
}

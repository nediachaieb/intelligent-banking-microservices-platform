package tn.nadia.ebankservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.nadia.ebankservice.entities.BankAccount;
import tn.nadia.ebankservice.enums.AccountType;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

     List<BankAccount> findByCustomerId(Long customerId);

     boolean existsByCustomerIdAndType(
             Long customerId,
             AccountType type
     );

}

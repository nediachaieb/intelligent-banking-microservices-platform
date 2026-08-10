package tn.nadia.ebankservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.nadia.ebankservice.entities.BankAccount;

import java.util.List;

public interface BanckAccountRepository extends JpaRepository<BankAccount, String> {

     BankAccount findByCustomerId(Long id);

}

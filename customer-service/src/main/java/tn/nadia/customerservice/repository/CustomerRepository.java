package tn.nadia.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.nadia.customerservice.entities.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
}
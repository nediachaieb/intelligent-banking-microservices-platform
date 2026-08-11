package tn.nadia.customerservice.service;

import org.springframework.stereotype.Service;
import tn.nadia.customerservice.entities.Customer;
import tn.nadia.customerservice.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {
    private  CustomerRepository customerRepository;
    // INJECTION DE DEPENDANCE VIA CONSTRUCTEUR
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }


    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
}

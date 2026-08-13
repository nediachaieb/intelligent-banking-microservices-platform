package tn.nadia.customerservice.service;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
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

 @McpTool(description = "Get all customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
@McpTool(description = "Get customer by id")
    public Customer getCustomerById(@McpToolParam (description = "the customer id") Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }
@McpTool(description = "Create a new customer")
    public Customer createCustomer(@McpToolParam (description = "the customer to save ") Customer customer) {
        return customerRepository.save(customer);
    }

@McpTool(description = "Check if a customer exists by email")
    public boolean existsByEmail( @McpToolParam (description = "The customer email")String email) {
        return customerRepository.existsByEmail(email);
    }
}

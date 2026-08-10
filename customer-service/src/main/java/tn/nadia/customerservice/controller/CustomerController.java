package tn.nadia.customerservice.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PathParam;
import org.springframework.web.bind.annotation.*;
import tn.nadia.customerservice.entities.Customer;
import tn.nadia.customerservice.service.CustomerService;

import java.util.List;
@RestController
public class CustomerController {

    private CustomerService customerService;

    private CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customers/{id}")

    public Customer getCustomerById( @PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("/customers")
    public Customer createCustomer(Customer customer) {
        return customerService.createCustomer(customer);
    }


}

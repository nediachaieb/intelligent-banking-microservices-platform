package tn.nadia.ebankservice.feign;

import org.springframework.stereotype.Component;
import tn.nadia.ebankservice.entities.Customer;

import java.util.List;

@Component
public class CustomerRestClientFallback
        implements CustomerRestClient {

    @Override
    public List<Customer> getAllCustomers() {
        return List.of();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return new Customer(id, "none", "none", "none");
    }
}

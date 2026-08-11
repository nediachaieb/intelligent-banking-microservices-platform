package tn.nadia.ebankservice.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.nadia.ebankservice.entities.Customer;

import java.util.List;

@FeignClient(
        name = "customer-service",
        fallback = CustomerRestClientFallback.class
)
public interface CustomerRestClient {

    @GetMapping("/customers")
    List<Customer> getAllCustomers();

    @GetMapping("/customers/{id}")
    Customer getCustomerById(@PathVariable Long id);
}

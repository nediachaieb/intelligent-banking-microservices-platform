package tn.nadia.ebankservice.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.nadia.ebankservice.entities.Customer;

@FeignClient(name = "customer-service")
public interface CustomerRestClient {
    @GetMapping("/customers/{id}")
    @CircuitBreaker(name = "customerService", fallbackMethod = "getCustomerByIdFallback")
    Customer getCustomerById(@PathVariable Long id);

    default Customer getCustomerByIdFallback (Long id, Exception e) {
        return new Customer(id,"none","none","none");
    }


}

package tn.nadia.customerservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tn.nadia.customerservice.entities.Customer;
import tn.nadia.customerservice.service.CustomerService;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
    @Bean
    public CommandLineRunner run(CustomerService customerService) {
        return args -> {

            for (int i = 1; i <=10; i++) {

                Customer customer = Customer.builder()
                        .firstName("First Name " + i)
                        .lastName("Last Name " + i)
                        .email("f.l" + i + "@gmail.com")
                        .phone("+2162300000" + i)
                        .build();

                if (!customerService.existsByEmail(customer.getEmail())) {
                    customerService.createCustomer(customer);
                }
            }
        };
    }
}




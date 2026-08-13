package tn.nadia.ebankservice;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tn.nadia.ebankservice.entities.BankAccount;
import tn.nadia.ebankservice.entities.Customer;
import tn.nadia.ebankservice.enums.AccountStatus;
import tn.nadia.ebankservice.enums.AccountType;
import tn.nadia.ebankservice.feign.CustomerRestClient;
import tn.nadia.ebankservice.service.BankService;

import java.sql.Date;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class EbankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner start(
            BankService bankService,
            @Qualifier("customerFeignClient") CustomerRestClient customerRestClient) {

        return args -> {

            List<Customer> customers =
                    customerRestClient.getAllCustomers();

            for (int i = 0; i < customers.size(); i++) {

                Customer customer = customers.get(i);

                BankAccount account = BankAccount.builder()
                        .balance(1000 + (i * 500))
                        .currency("TND")
                        .type(i % 2 == 0 ? AccountType.CURRENT_ACCOUNT : AccountType.SAVING_ACCOUNT)
                        .status(i % 2 == 0 ? AccountStatus.ACTIVATED : AccountStatus.SUSPENDED)
                        .customerId(customer.getId())

                        .build();

                bankService.saveBankAccount(account);
            }
        };
    }

}


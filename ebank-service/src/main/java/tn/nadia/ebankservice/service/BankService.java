package tn.nadia.ebankservice.service;

import org.springframework.stereotype.Service;
import tn.nadia.ebankservice.entities.BankAccount;
import tn.nadia.ebankservice.entities.Customer;
import tn.nadia.ebankservice.feign.CustomerRestClient;
import tn.nadia.ebankservice.repository.BanckAccountRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service

public class BankService {

    private BanckAccountRepository banckAccountRepository;
    private CustomerRestClient customerRestClient;


    public BankService(BanckAccountRepository banckAccountRepository , CustomerRestClient customerRestClient) {
        this.banckAccountRepository = banckAccountRepository;
        this.customerRestClient = customerRestClient;
    }


    public List<BankAccount> getAllBankAccounts() {
        return banckAccountRepository.findAll();
    }

    public BankAccount getBankAccountById(String id) {
        BankAccount banckAccount= banckAccountRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank account not found"));

        banckAccount.setCustomer(customerRestClient.getCustomerById(banckAccount.getCustomerId()));
        return banckAccount;
    }

    public BankAccount saveBankAccount(BankAccount bankAccount) {
        try {

           Customer customer= customerRestClient.getCustomerById(bankAccount.getCustomerId());
            bankAccount.setCreatedAt(new Date());
            return banckAccountRepository.save(bankAccount);
        } catch (Exception e) {
            throw new RuntimeException("Customer not found");
        }

    }

}

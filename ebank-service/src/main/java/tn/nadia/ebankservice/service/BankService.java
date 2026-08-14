package tn.nadia.ebankservice.service;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tn.nadia.ebankservice.entities.BankAccount;
import tn.nadia.ebankservice.entities.Customer;
import tn.nadia.ebankservice.feign.CustomerRestClient;
import tn.nadia.ebankservice.repository.BanckAccountRepository;

import java.util.Date;
import java.util.List;

@Service

public class BankService {

    private BanckAccountRepository banckAccountRepository;
    private CustomerRestClient customerRestClient;


    public BankService(BanckAccountRepository banckAccountRepository , @Qualifier("customerFeignClient") CustomerRestClient customerRestClient) {
        this.banckAccountRepository = banckAccountRepository;
        this.customerRestClient = customerRestClient;
    }


    @McpTool(description = "get all bank accounts with customer details")
    public List<BankAccount> getAllBankAccounts() {

        List<BankAccount> bankAccounts = banckAccountRepository.findAll();

        bankAccounts.forEach(account -> {
            account.setCustomer(
                    customerRestClient.getCustomerById(account.getCustomerId())
            );
        });

        return bankAccounts;
    }
    @McpTool(description = "get bank account by id with customer details")
    public BankAccount getBankAccountById(@McpToolParam(description = "the bank account id") String id) {
        BankAccount bankAccount= banckAccountRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank account not found"));

        bankAccount.setCustomer(customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }
    @McpTool(description = "save a new bank account for an existing customer")
    public BankAccount saveBankAccount(@McpToolParam (description = "the bank account to save (balance, currency, accountType, accountStatus ,customerID) ") BankAccount bankAccount) {
        try {

           Customer customer= customerRestClient.getCustomerById(bankAccount.getCustomerId());
            bankAccount.setCreatedAt(new Date());
            return banckAccountRepository.save(bankAccount);
        } catch (Exception e) {
            throw new RuntimeException("Customer not found");
        }

    }

}

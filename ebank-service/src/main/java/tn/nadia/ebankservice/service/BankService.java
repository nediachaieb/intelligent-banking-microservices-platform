package tn.nadia.ebankservice.service;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tn.nadia.ebankservice.entities.BankAccount;
import tn.nadia.ebankservice.entities.Customer;
import tn.nadia.ebankservice.enums.AccountType;
import tn.nadia.ebankservice.feign.CustomerRestClient;
import tn.nadia.ebankservice.repository.BanckAccountRepository;

import java.util.Date;
import java.util.List;

@Service
public class BankService {

    private final BanckAccountRepository banckAccountRepository;
    private final CustomerRestClient customerRestClient;


    public BankService(
            BanckAccountRepository banckAccountRepository,
            @Qualifier("customerFeignClient")
            CustomerRestClient customerRestClient) {

        this.banckAccountRepository = banckAccountRepository;
        this.customerRestClient = customerRestClient;
    }


    // =====================================================
    // GET ALL ACCOUNTS
    // =====================================================

    @McpTool(
            description = "get all bank accounts with customer details"
    )
    public List<BankAccount> getAllBankAccounts() {

        List<BankAccount> bankAccounts =
                banckAccountRepository.findAll();

        bankAccounts.forEach(account -> {

            Customer customer =
                    customerRestClient.getCustomerById(
                            account.getCustomerId()
                    );

            account.setCustomer(customer);
        });

        return bankAccounts;
    }


    // =====================================================
    // GET ACCOUNT BY ID
    // =====================================================

    @McpTool(
            description = "get bank account by id with customer details"
    )
    public BankAccount getBankAccountById(
            @McpToolParam(
                    description = "the bank account id"
            )
            String id) {

        BankAccount bankAccount =
                banckAccountRepository.findById(Long.valueOf(id))
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Bank account not found"
                                )
                        );

        Customer customer =
                customerRestClient.getCustomerById(
                        bankAccount.getCustomerId()
                );

        bankAccount.setCustomer(customer);

        return bankAccount;
    }


    // =====================================================
    // GET ALL ACCOUNTS OF ONE CUSTOMER
    // =====================================================

    @McpTool(
            description = "get all bank accounts of a customer"
    )
    public List<BankAccount> getAccountsByCustomerId(
            @McpToolParam(
                    description = "the customer id"
            )
            Long customerId) {

        // Vérifie d'abord que le client existe
        customerRestClient.getCustomerById(customerId);

        List<BankAccount> accounts =
                banckAccountRepository
                        .findByCustomerId(customerId);

        accounts.forEach(account -> {

            Customer customer =
                    customerRestClient.getCustomerById(
                            account.getCustomerId()
                    );

            account.setCustomer(customer);
        });

        return accounts;
    }


    // =====================================================
    // SAVE BANK ACCOUNT
    // =====================================================

    @McpTool(
            description = "save a new bank account for an existing customer"
    )
    public BankAccount saveBankAccount(
            @McpToolParam(
                    description = "the bank account to save"
            )
            BankAccount bankAccount) {

        // 1. Vérifier que le customerId existe
        Customer customer =
                customerRestClient.getCustomerById(
                        bankAccount.getCustomerId()
                );

        if (customer == null) {
            throw new RuntimeException(
                    "Customer not found"
            );
        }


        // 2. Vérifier que ce type de compte
        // n'existe pas déjà pour ce client
        boolean accountAlreadyExists =
                banckAccountRepository
                        .existsByCustomerIdAndType(
                                bankAccount.getCustomerId(),
                                bankAccount.getType()
                        );

        if (accountAlreadyExists) {

            throw new RuntimeException(
                    "Customer already has a "
                            + bankAccount.getType()
            );
        }


        // 3. Date de création
        bankAccount.setCreatedAt(new Date());


        // 4. Sauvegarde
        BankAccount savedAccount =
                banckAccountRepository.save(bankAccount);


        // 5. Ajouter les informations du client
        savedAccount.setCustomer(customer);

        return savedAccount;
    }


    // =====================================================
    // UTILISÉ PAR LE COMMAND LINE RUNNER
    // =====================================================

    public boolean accountExists(
            Long customerId,
            AccountType type) {

        return banckAccountRepository
                .existsByCustomerIdAndType(
                        customerId,
                        type
                );
    }
}
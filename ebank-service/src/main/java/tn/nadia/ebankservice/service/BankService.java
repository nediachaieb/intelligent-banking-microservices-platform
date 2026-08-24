package tn.nadia.ebankservice.service;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tn.nadia.ebankservice.entities.BankAccount;
import tn.nadia.ebankservice.entities.Customer;
import tn.nadia.ebankservice.enums.AccountType;
import tn.nadia.ebankservice.feign.CustomerRestClient;
import tn.nadia.ebankservice.repository.BankAccountRepository;

import java.util.Date;
import java.util.List;

@Service
public class BankService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRestClient customerRestClient;


    public BankService(
            BankAccountRepository bankAccountRepository,
            @Qualifier("customerFeignClient")
            CustomerRestClient customerRestClient) {

        this.bankAccountRepository = bankAccountRepository;
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
                bankAccountRepository.findAll();

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
                bankAccountRepository.findById(Long.valueOf(id))
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

    // ==============================================
// GET ALL ACCOUNTS OF ONE CUSTOMER
// ==============================================
    @McpTool(
            description = """
             
                Use this tool when the user asks for the bank accounts
                of one customer using a customer id.
              
                """
    )
    public List<BankAccount> getAccountsByCustomerId(

            @McpToolParam(
                    description = "The unique customer id used to retrieve only this customer's bank accounts"
            )
            Long customerId
    ) {

        // Vérifie que le client existe
        Customer customer = customerRestClient.getCustomerById(customerId);

        // Récupère uniquement les comptes de ce client
        List<BankAccount> accounts =
                bankAccountRepository.findByCustomerId(customerId);

        // Ajoute les informations du client à chaque compte
        accounts.forEach(account -> account.setCustomer(customer));

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
                bankAccountRepository
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
                bankAccountRepository.save(bankAccount);


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

        return bankAccountRepository
                .existsByCustomerIdAndType(
                        customerId,
                        type
                );
    }
}
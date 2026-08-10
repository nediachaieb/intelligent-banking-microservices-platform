package tn.nadia.ebankservice.controller;

import org.springframework.web.bind.annotation.*;
import tn.nadia.ebankservice.entities.BankAccount;
import tn.nadia.ebankservice.service.BankService;

import java.util.List;

@RestController
public class BankController {

    private BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

@GetMapping("/bankAccounts")
    public List<BankAccount> getAllBankAccounts() {
        return bankService.getAllBankAccounts();
    }
@GetMapping("/bankAccounts/{id}")
    public BankAccount getBankAccountById( @PathVariable String id) {
        return bankService.getBankAccountById(id);

    }
@PostMapping("/bankAccounts")
    public BankAccount saveBankAccount(@RequestBody BankAccount bankAccount) {
        return bankService.saveBankAccount(bankAccount);
    }

}

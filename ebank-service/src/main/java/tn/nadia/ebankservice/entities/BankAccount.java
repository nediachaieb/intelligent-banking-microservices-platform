package tn.nadia.ebankservice.entities;

import jakarta.persistence.*;
import lombok.*;
import tn.nadia.ebankservice.enums.AccountStatus;
import tn.nadia.ebankservice.enums.AccountType;

import java.util.Date;
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount {
@Id  @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private double balance;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private Long customerId;
    @Transient
    private  Customer customer;

}


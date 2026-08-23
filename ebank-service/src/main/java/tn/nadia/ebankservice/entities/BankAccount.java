package tn.nadia.ebankservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import tn.nadia.ebankservice.enums.AccountStatus;
import tn.nadia.ebankservice.enums.AccountType;

import java.util.Date;
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount {
@Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Africa/Tunis")
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


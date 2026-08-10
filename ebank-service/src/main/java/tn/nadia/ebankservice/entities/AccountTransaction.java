package tn.nadia.ebankservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import tn.nadia.ebankservice.enums.TransactionType;


import java.util.Date;
@Entity @NoArgsConstructor
@AllArgsConstructor @Builder
public class AccountTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private double amount;
    private String description;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BankAccount bankAccount;
}
/*
 BankAccount        = le compte bancaire
AccountTransaction = une opération effectuée sur ce compte
 */
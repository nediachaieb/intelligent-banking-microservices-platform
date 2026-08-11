package tn.nadia.ebankservice.entities;

import lombok.*;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;


}

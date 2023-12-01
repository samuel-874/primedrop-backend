package com.jme.spatch.backend.model.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jme.spatch.backend.model.wallet.entity.Wallet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserEntity {

    @Id
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_seq")
    private long id;
    private String fullName;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phoneNo;
    @Enumerated(EnumType.STRING)
    private Role role;
    private  long registrationDate = System.currentTimeMillis();
    @Enumerated(EnumType.STRING)
    private Status status;
    private String imagePath;
    private LocalDateTime lastLogin;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Wallet wallet;
}

package com.vako.caucasus.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Entity
@Getter
@Setter


public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    String firstName;
    String lastName;
    @Column(unique = true)
    String personalNumber;
    @Column(unique = true)
    String phoneNumber;
    BigDecimal balance;


}

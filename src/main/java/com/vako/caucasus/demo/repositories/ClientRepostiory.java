package com.vako.caucasus.demo.repositories;

import com.vako.caucasus.demo.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ClientRepostiory extends JpaRepository<Client, Long> {
    public Optional<Client> findByPhoneNumber(String number);
    public Optional<Client> findByPersonalNumber(String number);


    @Modifying
    @Query("UPDATE Client client set client.balance = client.balance + :amount where client.id = :userId and client.balance+:amount >= 0")
    public Integer UpdateBalanceIfNotNull(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
}

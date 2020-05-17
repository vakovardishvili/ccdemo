package com.vako.caucasus.demo.services;

import com.vako.caucasus.demo.models.Client;
import com.vako.caucasus.demo.models.Transactions;
import com.vako.caucasus.demo.repositories.ClientRepostiory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TrasnactionServiceTest {
    @Autowired
    TrasnactionService trasnactionService;

    @Autowired
    ClientRepostiory clientRepostiory;

    @PersistenceContext
    EntityManager manager;


    @Test
    @Transactional
    void make_BalanceNotEnough() {
        Client client = new Client();
        client.setFirstName("vako");
        client.setLastName("vardishvili");
        client.setPersonalNumber("01008060181");
        client.setPhoneNumber("599904530");
        client.setBalance(new BigDecimal(87));
        clientRepostiory.save(client);
        assertThrows(TrasnactionService.NotEnoughMoney.class, () -> {
            trasnactionService.make(client, BigDecimal.valueOf(88), Transactions.TrTypes.PHONE_OUT);
        });
    }




    @Test
    @Transactional
    void make_correct() throws TrasnactionService.NotEnoughMoney {
        Client client = new Client();
        client.setFirstName("vako");
        client.setLastName("vardishvili");
        client.setPersonalNumber("01008060181");
        client.setPhoneNumber("599904530");
        client.setBalance(new BigDecimal(87));
        clientRepostiory.save(client);
        trasnactionService.make(client, BigDecimal.valueOf(87), Transactions.TrTypes.PHONE_OUT);
        manager.refresh(client);
        assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(client.getBalance()));
    }

}

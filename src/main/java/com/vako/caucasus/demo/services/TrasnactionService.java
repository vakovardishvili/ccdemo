package com.vako.caucasus.demo.services;

import com.vako.caucasus.demo.models.Client;
import com.vako.caucasus.demo.models.Transactions;
import com.vako.caucasus.demo.repositories.ClientRepostiory;
import com.vako.caucasus.demo.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TrasnactionService {


    @Getter
    @AllArgsConstructor
    public static class NotEnoughMoney extends Exception {
        Client client;

        @Override
        public String getMessage() {
            return "Not enough money to make transaction :(";
        }
    }

    private final
    TransactionRepository transactionRepository;
    private final ClientRepostiory clientRepostiory;

    public TrasnactionService(TransactionRepository transactionRepository, ClientRepostiory clientRepostiory) {
        this.transactionRepository = transactionRepository;
        this.clientRepostiory = clientRepostiory;
    }

    @Transactional(rollbackFor = Exception.class)
    public void make(Client client, BigDecimal amount, Transactions.TrTypes type) throws NotEnoughMoney {
        Transactions transaction = new Transactions();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setUserId(client.getId());
        transactionRepository.save(transaction);
        int i = clientRepostiory.UpdateBalanceIfNotNull(client.getId(), amount.multiply(type.getDirection()));
        if (i == 0) {
            throw new NotEnoughMoney(client);
        }
    }

    public List<Transactions> findByUserId(Long id){
        return transactionRepository.findByUserId(id);
    }
}

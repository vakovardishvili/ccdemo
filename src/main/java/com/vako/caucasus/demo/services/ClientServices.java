package com.vako.caucasus.demo.services;


import com.vako.caucasus.demo.models.Client;
import com.vako.caucasus.demo.repositories.ClientRepostiory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServices {
     ClientRepostiory clientRepostiory;

    public ClientServices(ClientRepostiory clientRepostiory) {
        this.clientRepostiory = clientRepostiory;
    }

    public Optional<Client> findByPhoneNumber(String phoneNumber){
                return clientRepostiory.findByPhoneNumber(phoneNumber);
    }

    public Optional<Client> findByPersonalNumber(String personalNumber){
        return clientRepostiory.findByPersonalNumber(personalNumber);
    }

    public Client findById(Long id){
        return clientRepostiory.findById(id).get();
    }


}

package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllCLients();
    List<ClientDTO> getAllCLientsDTO();

    Client getClientById(Long id);

    Client getClientByEmail(String email);

    void saveCLient(Client client);

}

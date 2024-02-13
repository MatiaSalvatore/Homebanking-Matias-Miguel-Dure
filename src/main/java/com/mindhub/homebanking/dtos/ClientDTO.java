package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO{
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts;
    private List<ClientLoanDTO> clientloans;
    private Set<CardDTO> cards;

    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());

        this.clientloans = client.getLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toList());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<ClientLoanDTO> getClientloans() {
        return clientloans;
    }

    public ClientDTO() {
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
}

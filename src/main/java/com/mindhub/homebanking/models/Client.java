package com.mindhub.homebanking.models;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.enums.UserRoles;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password, username;

    private UserRoles Role;

    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    Set<ClientLoan> loans = new HashSet<>();

    @OneToMany(mappedBy = "cardOwner",fetch =FetchType.EAGER)
    Set<Card> cards  = new HashSet<>();



    //Constructores
    public Client() { }

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public UserRoles getRole() {
        return Role;
    }

    public void setRole(UserRoles role) {
        Role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return firstName + " " + lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<ClientLoan> getLoans() {
        return loans;
    }

    public void setLoans(Set<ClientLoan> loans) {
        this.loans = loans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    //Métodos
    public void addAccount(Account account){
        account.setOwner(this);
        accounts.add(account);
    }

    public void addCard(Card card){
        card.setCardOwner(this);
        card.setCardHolder(this.firstName+" "+this.lastName);
        cards.add(card);
    }
    public void addLoan(ClientLoan clientloan){
        clientloan.setClient(this);
        loans.add(clientloan);
    }
    public List<ClientLoanDTO> getClientLoans() {
        return this.getLoans().stream().map(ClientLoanDTO::new).toList();
    }

}

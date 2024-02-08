package com.mindhub.homebanking.models;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
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

    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    Set<ClientLoan> loans = new HashSet<>();

    //Constructores
    public Client() { }

    public Client(String first, String last,String mail) {
        firstName = first;
        lastName = last;
        email = mail;

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

    //Métodos
    public void addAccount(Account account){
        account.setOwner(this);
        accounts.add(account);
    }
    public void addLoan(ClientLoan clientloan){
        clientloan.setClient(this);
        loans.add(clientloan);
    }
    public List<ClientLoanDTO> getClientLoans() {
        return this.getLoans().stream().map(ClientLoanDTO::new).toList();
    }

}

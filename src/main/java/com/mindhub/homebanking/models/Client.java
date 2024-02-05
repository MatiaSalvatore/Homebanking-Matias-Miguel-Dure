package com.mindhub.homebanking.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "native", strategy = "native")

    @OneToMany(mappedBy="owner", fetch=FetchType.EAGER)

    Set<Account> accounts = new HashSet<>();
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    public Client() { }

    public Client(String first, String last,String mail) {
        firstName = first;
        lastName = last;
        email = mail;
    }

    public void addAccount(Account account){
        account.setOwner(this);
        accounts.add(account);
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}

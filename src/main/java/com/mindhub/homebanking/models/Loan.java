package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="native")
    private long id;
    private String name;
    private Double maxAmount;

    @ElementCollection
    @Column(name="payment")
    private List<Integer> payments;
    @OneToMany(mappedBy = "loan",fetch = FetchType.EAGER)
    Set<ClientLoan> clientloans;

    public Loan(String name, Double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public void addClientloan(ClientLoan clientloan){
        clientloan.setLoan(this);
        clientloans.add(clientloan);
    }

    public List<Client> getClients() {
        return clientloans.stream().map(sub -> sub.getClient()).collect(toList());
    }


}

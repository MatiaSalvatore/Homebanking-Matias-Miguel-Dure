package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.time.LocalDate;
import java.util.Set;

public class ClientLoanDTO {
    private Long id;
    private Double amount;
    private Integer payments;
    private String name;
    private Long loanId;
    private Long clientId;

    public ClientLoanDTO(ClientLoan clientloan) {
        this.id = clientloan.getId();
        this.amount = clientloan.getAmount();
        this.payments = clientloan.getPayments();
        this.name =  clientloan.getLoan().getName();
        this.loanId = clientloan.getLoan().getId();
        this.clientId = clientloan.getClient().getId();
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getName() {
        return name;
    }

    public Long getLoanId() {
        return loanId;
    }

    public Long getClientId() {
        return clientId;
    }
}

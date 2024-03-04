package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

public record LoanRequestDTO(String name, Double amount, Integer payments, String accountNumber) {
}

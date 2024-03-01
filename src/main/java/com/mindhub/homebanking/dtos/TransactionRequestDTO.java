package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.TransactionType;

public record TransactionRequestDTO(String ogAccount, String destAccount, String detail, Double amount) {
}

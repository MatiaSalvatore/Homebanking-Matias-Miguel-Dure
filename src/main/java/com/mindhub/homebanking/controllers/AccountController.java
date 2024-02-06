package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @GetMapping("/hello")
    public String getClients(){
        return "Hello accounts!";
    }
    @Autowired
    private AccountRepository accountrepository;
    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> obtainAccounts(){
        List<Account> clients = accountrepository.findAll();
        return new ResponseEntity<>(clients.stream().map(AccountDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> obtainAccountById(@PathVariable Long id){
        Account account = accountrepository.findById(id).orElse(null);

        if (account == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found, ");
        }
        AccountDTO accountDTO = new AccountDTO(account);
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }
}

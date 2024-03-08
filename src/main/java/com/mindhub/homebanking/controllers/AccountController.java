package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/accounts")
public class AccountController {
    @GetMapping("/hello")
    public String getClients(){
        return "Hello accounts!";
    }
    @Autowired
    private AccountRepository accountrepository;
    @Autowired
    private ClientRepository clientrepository;
    @Autowired
    private ClientService clientService;
    private String RandomNumberGenerator(){
        Random rand = new Random();
        String randomaccountnumber = String.valueOf(rand.nextInt(100000));
        return randomaccountnumber;
    }
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

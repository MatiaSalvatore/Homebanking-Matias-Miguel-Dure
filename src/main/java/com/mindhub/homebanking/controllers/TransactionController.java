package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionRequestDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping()
    ResponseEntity<?> postTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO){
        String usermail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(usermail);
        Set<Account> ogclientaccounts = client.getAccounts();
        Account ogAccount = accountRepository.findByNumber(transactionRequestDTO.ogAccount());
        Account destAccount = accountRepository.findByNumber(transactionRequestDTO.destAccount());

        if (!ogclientaccounts.contains(ogAccount)){
            return new ResponseEntity<>("The source account does not correspond to the authenticated user.",HttpStatus.FORBIDDEN);
        }

        if (ogAccount.getBalance() < transactionRequestDTO.amount()){
            return new ResponseEntity<>("Not enough funds in source account.",HttpStatus.FORBIDDEN);
        }

        if (transactionRequestDTO.ogAccount().isBlank()
                ||transactionRequestDTO.destAccount().isBlank()
                ||transactionRequestDTO.detail().isBlank()){
            return new ResponseEntity<>("All fields must be completed.",HttpStatus.FORBIDDEN);
        }


        if (transactionRequestDTO.ogAccount().equals(transactionRequestDTO.destAccount())){
            return new ResponseEntity<>("The source account is the same as the destination account",HttpStatus.FORBIDDEN);
        }

        if (transactionRequestDTO.amount() <= 0){
            return new ResponseEntity<>("You must specify an amount, it cannot be zero or negative",HttpStatus.FORBIDDEN);
        }

        Transaction ogtransaction = new Transaction((-1.0)*transactionRequestDTO.amount(),
                LocalDate.now(),
                transactionRequestDTO.detail(),
                TransactionType.DEBIT);

        Transaction destTransaction = new Transaction(transactionRequestDTO.amount(),
                LocalDate.now(),
                transactionRequestDTO.detail(),
                TransactionType.CREDIT);

        ogAccount.addTransaction(ogtransaction);
        ogAccount.setBalance(ogAccount.getBalance()-transactionRequestDTO.amount());
        destAccount.addTransaction(destTransaction);
        destAccount.setBalance(destAccount.getBalance()+transactionRequestDTO.amount());
        transactionRepository.save(ogtransaction);
        transactionRepository.save(destTransaction);


        return new ResponseEntity<>("Transaction successful", HttpStatus.CREATED);
    }

    //TODO: AGREGAR TRY CATCH
}

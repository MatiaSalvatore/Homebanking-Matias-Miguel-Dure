package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionRequestDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping()
    ResponseEntity<?> postTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO){
        if (transactionRequestDTO.ogAccount().isBlank()
                ||transactionRequestDTO.destAccount().isBlank()
                ||transactionRequestDTO.detail().isBlank()){
            return new ResponseEntity<>("All fields must be completed.",HttpStatus.FORBIDDEN);
        }

        if (transactionRequestDTO.amount() <= 0){
            return new ResponseEntity<>("You must specify an amount, it cannot be zero or negative",HttpStatus.FORBIDDEN);
        }
        Account ogAccount = accountRepository.findByNumber(transactionRequestDTO.ogAccount());
        Account destAccount = accountRepository.findByNumber(transactionRequestDTO.destAccount());

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


        return new ResponseEntity<>("Transaction succesfull", HttpStatus.CREATED);
    }

}

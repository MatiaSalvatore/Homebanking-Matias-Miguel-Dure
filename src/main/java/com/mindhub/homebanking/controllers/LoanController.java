package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.LoanRequestDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    /*@GetMapping()
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).toList();
    }*/

    @Transactional
    @PostMapping()
    ResponseEntity<?> postLoan(@RequestBody LoanRequestDTO loanRequestDTO){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);
        List<String> clientAccounts = client.getAccounts().stream().map(Account::getNumber).toList();
        Loan currentLoan = loanRepository.findByname(loanRequestDTO.name());
        Account currentAccount = accountRepository.findByNumber(loanRequestDTO.accountNumber());

        if (loanRequestDTO.amount().isNaN()||loanRequestDTO.name().isBlank()||loanRequestDTO.accountNumber().isBlank()||loanRequestDTO.payments() == null){
            return new ResponseEntity<>("Fields cannot be empty",HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(currentAccount)){
            return new ResponseEntity<>("The specified account doesn't belong to the current Client",HttpStatus.FORBIDDEN);
        }
        if (currentAccount==null){
            return new ResponseEntity<>("The specified account doesn't exist.",HttpStatus.FORBIDDEN);
        }
        if (currentLoan == null){
            return new ResponseEntity<>("That type of Loan doesn't exist.",HttpStatus.FORBIDDEN);
        }
        if (!currentLoan.getPayments().contains(loanRequestDTO.payments())){
            return new ResponseEntity<>("Cannot set that amount of payments with that loan.",HttpStatus.FORBIDDEN);
        }
        if (currentLoan.getMaxAmount()<loanRequestDTO.amount()){
            return new ResponseEntity<>("Max amount with tat loan exceeded.",HttpStatus.FORBIDDEN);
        }


        ClientLoan clientLoan = new ClientLoan(client,currentLoan,
                loanRequestDTO.amount(),
                loanRequestDTO.payments());
        Transaction loanTransaction = new Transaction(loanRequestDTO.amount(),
                LocalDate.now(),
                loanRequestDTO.name()+" loan credited",
                TransactionType.CREDIT);

        currentAccount.addTransaction(loanTransaction);
        clientLoanRepository.save(clientLoan);
        transactionRepository.save(loanTransaction);

        return new ResponseEntity<>("Loan applied",HttpStatus.CREATED);
    }

}

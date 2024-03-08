package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardRequestDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/clients")
public class ClientController {
    @GetMapping("/hello")
    public String getClients(){
        return "Hello Clients!";
    }

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientService clientService;

    //Generador de número aleatorio para cuentas
    private int RandomNumberGenerator(){
        Random rand = new Random();
        int randomaccountnumber = 10000000 + rand.nextInt(90000000);
        return randomaccountnumber;
    }

    //Generador aleatorio de número de 4 dígitos (para tarjeta de crédito)
    private String rand4(){
        Random rand = new Random();
        String ramdom4digits = String.valueOf(rand.nextInt(9999));
        return ramdom4digits;
    }

    //Generador aleatorio de números de 3 dígitos (para CVV)
    private String rand3(){
        Random rand = new Random();
        String ramdom3digits = String.valueOf(rand.nextInt(999));
        return ramdom3digits;
    }


    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> obtainClients(){
        /*List<Client> clients = clientRepository.findAll();
        return new ResponseEntity<>(clients.stream().map(ClientDTO::new).collect(Collectors.toList()), HttpStatus.OK);*/
        return new ResponseEntity<>(clientService.getAllCLientsDTO(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtainClientById(@PathVariable Long id){
        Client client = clientService.getClientById(id);

        if (client == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found, sorry, try again later!");
        }
        ClientDTO clientDTO = new ClientDTO(client);
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getClient(){
        String usermail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(usermail);
        return ResponseEntity.ok(new ClientDTO(client));
    }

    @GetMapping("/current/accounts")
    public ResponseEntity<?> getAccounts(){
        String usermail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(usermail);
        Set<Account> accounts = client.getAccounts();
        return new ResponseEntity<>(accounts.stream().map(AccountDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }
    @PostMapping("/current/accounts")
    public ResponseEntity<?> addAccounts(){
        String accountnumber = "VIN-"+RandomNumberGenerator();
        if (accountRepository.findByNumber(accountnumber) != null){
            accountnumber = "VIN-"+RandomNumberGenerator();
        }
        String usermail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(usermail);

        if (client.getAccounts().size() >= 3){
            return new ResponseEntity<>("You cannot have more than 3 accounts.", HttpStatus.FORBIDDEN);
        }
        Account newuseraccount = new Account(accountnumber, LocalDate.now(),0.0);
        client.addAccount(newuseraccount);
        accountRepository.save(newuseraccount);
        return ResponseEntity.ok("Account created");
    }
    @GetMapping("/current/cards")
    public ResponseEntity<?> getCards(){
        String usermail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(usermail);
        Set<Card> accounts = client.getCards();
        return new ResponseEntity<>(accounts.stream().map(CardDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/current/cards")
    public ResponseEntity<?> addCards(@RequestBody CardRequestDTO cardRequestDTO){
        String usermail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientService.getClientByEmail(usermail);
        Set<Card> cardlist = client.getCards();
        Set<Card> debitCard = new HashSet<>();
        Set<Card> creditCard = new HashSet<>();


        for(Card card : cardlist){
            if (card.getType() == CardType.CREDIT){
                creditCard.add(card);
            }
            else if  (card.getType() == CardType.DEBIT){
                debitCard.add(card);
            }
        }

        if (cardRequestDTO.cardtype() == CardType.DEBIT && debitCard.size() >= 3 || cardRequestDTO.cardtype() == CardType.CREDIT && creditCard.size() >= 3){
            return new ResponseEntity<>("You cannot have more than 3 cards of each type.", HttpStatus.FORBIDDEN);
        }

        Card newcard = new Card(rand4()+"-"+rand4()+"-"+rand4()+"-"+rand4(),
                rand3(),
                cardRequestDTO.cardtype(),
                cardRequestDTO.color(),
                LocalDate.now(),
                LocalDate.now().plusYears(8));
        client.addCard(newcard);
        cardRepository.save(newcard);
        return ResponseEntity.ok("Card created");
    }
}

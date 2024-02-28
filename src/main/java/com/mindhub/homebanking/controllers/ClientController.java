package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardRequestDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    private int RandomNumberGenerator(){
        Random rand = new Random();
        int randomaccountnumber = rand.nextInt(100000);
        return randomaccountnumber;
    }

    private String rand4(){
        Random rand = new Random();
        String ramdom4digits = String.valueOf(rand.nextInt(9999));
        return ramdom4digits;
    }

    private String rand3(){
        Random rand = new Random();
        String ramdom3digits = String.valueOf(rand.nextInt(999));
        return ramdom3digits;
    }

    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> obtainClients(){
        List<Client> clients = clientRepository.findAll();
        return new ResponseEntity<>(clients.stream().map(ClientDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtainClientById(@PathVariable Long id){
        Client client = clientRepository.findById(id).orElse(null);

        if (client == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found, sorry, try again later!");
        }
        ClientDTO clientDTO = new ClientDTO(client);
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getClient(){
        String usermail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(usermail);
        return ResponseEntity.ok(new ClientDTO(client));
    }

    @GetMapping("/current/accounts")
    public ResponseEntity<?> getAccounts(){
        String usermail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(usermail);
        return ResponseEntity.ok(new ClientDTO((client)).getAccounts());
    }
    @PostMapping("/current/accounts")
    public ResponseEntity<?> addAccounts(){
        String usermail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(usermail);
        if (client.getAccounts().size() >= 3){
            return new ResponseEntity<>("You cannot have more than 3 accounts.", HttpStatus.FORBIDDEN);
        }
        Account newuseraccount = new Account("VIN-"+RandomNumberGenerator(), LocalDate.now(),0.0);
        client.addAccount(newuseraccount);
        accountRepository.save(newuseraccount);
        return ResponseEntity.ok("Account created");
    }

    @PostMapping("/current/cards")
    public ResponseEntity<?> addCards(@RequestBody CardRequestDTO cardRequestDTO){
        String usermail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(usermail);
        if (client.getCards().size() >= 3){
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

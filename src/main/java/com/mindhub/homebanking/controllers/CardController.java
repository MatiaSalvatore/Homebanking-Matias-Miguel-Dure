package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardRequestDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    @Autowired
    private CardRepository cardrepository;
    @Autowired
    private ClientRepository clientrepository;

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


    @PostMapping("/add")
    public ResponseEntity<?> addAccount(@RequestBody CardRequestDTO cardRequestDTO){
        String userMail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientrepository.findByEmail(userMail);

        if (client.getCards().size() >= 3){
            return new ResponseEntity<>("You cannot have more than 3 cards.", HttpStatus.FORBIDDEN);
        }
        Card newcard = new Card(rand4()+"-"+rand4()+"-"+rand4()+"-"+rand4(),
                rand3(),
                cardRequestDTO.cardtype(),
                cardRequestDTO.color(),
                LocalDate.now(),
                LocalDate.now().plusYears(8));
        client.addCard(newcard);
        cardrepository.save(newcard);
        clientrepository.save(client);
        return new ResponseEntity<>("The card was succesfully created",HttpStatus.CREATED);
    }

}

package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoginDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.enums.UserRoles;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.securityServices.JwtUtilService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;


    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //Generador de número aleatorio para cuentas
    private int RandomNumberGenerator(){
        Random rand = new Random();
        int randomaccountnumber = 10000000 + rand.nextInt(90000000);
        return randomaccountnumber;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(),loginDTO.password()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());
            final String jwt = jwtUtilService.generateToken(userDetails);
            Client client = clientService.getClientByEmail(loginDTO.email());
            //Agregar validaciones de LOGIN

            return ResponseEntity.ok(jwt);
        }catch (Exception e) {
            return new ResponseEntity<>("Incorrect", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO){

        //Corroboramos que un cliente con el mismo mail no exista:

        Client firstCheck = clientService.getClientByEmail(registerDTO.email());
        if (firstCheck != null){
            return new ResponseEntity<>("Client already exists", HttpStatus.FORBIDDEN);
        }
        //Corroboramos que se ingresen todos los datos

        if(registerDTO.firstname().isBlank() || registerDTO.lastname().isBlank() || registerDTO.password().isBlank() || registerDTO.email().isBlank()){
            return new ResponseEntity<>("Fill in all required fields before proceeding further.", HttpStatus.NOT_FOUND);
        }

        //Creo al cliente y lo guardo en el repo.
        Client newClient = new Client(registerDTO.firstname(),registerDTO.lastname(),registerDTO.email(),passwordEncoder.encode(registerDTO.password()));
        newClient.setRole(UserRoles.USER);
        clientService.saveCLient(newClient);

        //Creo la cuenta que viene con el cliente, lo linkeo al mismo y guardo en su respectivo repo.

        Account newuseraccount = new Account("VIN-"+RandomNumberGenerator(), LocalDate.now(),0.0);
        newClient.addAccount(newuseraccount);
        accountRepository.save(newuseraccount);

        return new ResponseEntity<>("Client created", HttpStatus.CREATED);


    }
    }


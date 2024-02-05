package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	public Client clientOne = new Client("Matías","Dure","matiasmigueldure@gmail.com");
	public Account acc1 = new Account("A45321", LocalDate.now(),"20",7000);


	@Bean
	public CommandLineRunner initData(ClientRepository repository){
		return args -> {
			clientOne.addAccount(acc1);
			repository.save(clientOne);
		};
	}
}

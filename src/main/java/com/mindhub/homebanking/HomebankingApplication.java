package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
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
	@Bean
	public CommandLineRunner initData(ClientRepository clientrepository, AccountRepository accountrepository){
		return args -> {
			Client clientOne = new Client("Matías","Dure","matiasmigueldure@gmail.com");
			Client clientTwo = new Client("Gaby","Sosa","gabysosa@gmail.com");
			Account accOne = new Account("A01", LocalDate.now(),5000.0);
			Account accTwo = new Account("A02", LocalDate.now().plusDays(1),7500.0);
			Account accTri = new Account("B01", LocalDate.now().minusDays(5),8500.0);
			Account accFour = new Account("B02", LocalDate.now(),3000.0);

			clientOne.addAccount(accOne);
			clientOne.addAccount(accTwo);
			clientTwo.addAccount(accTri);
			clientTwo.addAccount(accFour);

			clientrepository.save(clientOne);
			clientrepository.save(clientTwo);
			accountrepository.save(accOne);
			accountrepository.save(accTwo);
			accountrepository.save(accTri);
			accountrepository.save(accFour);
		};
	}
}

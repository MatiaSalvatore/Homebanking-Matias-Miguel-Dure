package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
	public CommandLineRunner initData(ClientRepository clientrepository, AccountRepository accountrepository, TransactionRepository transactionrepository){
		return args -> {
			//Clients
			Client clientOne = new Client("Matías","Dure","matiasmigueldure@gmail.com");
			Client clientTwo = new Client("Gaby","Sosa","gabysosa@gmail.com");
			//Accounts
			Account accOne = new Account("A01", LocalDate.now(),5000.0);
			Account accTwo = new Account("A02", LocalDate.now().plusDays(1),7500.0);
			Account accTri = new Account("B01", LocalDate.now().minusDays(5),8500.0);
			Account accFour = new Account("B02", LocalDate.now(),3000.0);
			//Transactions
			Transaction t1 = new Transaction(1000.0,LocalDate.now(),"Compra de pizza", TransactionType.DEBIT);
			Transaction t2 = new Transaction(70000.0,LocalDate.now(),"Compra de PC", TransactionType.DEBIT);
			Transaction t3 = new Transaction(217000.0,LocalDate.now(),"Depósito de sueldo", TransactionType.CREDIT);
			Transaction t4 = new Transaction(500.0,LocalDate.now(),"Impuestos", TransactionType.DEBIT);
			Transaction t5 = new Transaction(8000.0,LocalDate.now(),"Bono de navidad", TransactionType.CREDIT);
			Transaction t6 = new Transaction(10000.0,LocalDate.now(),"Compra de Mercadolibre", TransactionType.DEBIT);
			Transaction t7 = new Transaction(15000.0,LocalDate.now(),"Pago por trabajo", TransactionType.CREDIT);
			Transaction t8 = new Transaction(8000.0,LocalDate.now(),"QUINI", TransactionType.CREDIT);

			clientOne.addAccount(accOne);
			clientOne.addAccount(accTwo);
			clientTwo.addAccount(accTri);
			clientTwo.addAccount(accFour);
			accOne.addTransaction(t1);
			accOne.addTransaction(t2);
			accTwo.addTransaction(t3);
			accTwo.addTransaction(t4);
			accTri.addTransaction(t5);
			accTri.addTransaction(t6);
			accFour.addTransaction(t7);
			accFour.addTransaction(t8);



			clientrepository.save(clientOne);
			clientrepository.save(clientTwo);
			accountrepository.save(accOne);
			accountrepository.save(accTwo);
			accountrepository.save(accTri);
			accountrepository.save(accFour);
			transactionrepository.save(t1);
			transactionrepository.save(t2);
			transactionrepository.save(t3);
			transactionrepository.save(t4);
			transactionrepository.save(t5);
			transactionrepository.save(t6);
			transactionrepository.save(t7);
			transactionrepository.save(t8);

		};
	}
}

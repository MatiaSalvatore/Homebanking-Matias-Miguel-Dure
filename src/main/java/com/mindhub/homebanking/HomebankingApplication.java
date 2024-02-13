package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientrepository, AccountRepository accountrepository, TransactionRepository transactionrepository, LoanRepository loanrepository, ClientLoanRepository clientloanrepository, CardRepository cardrepository ){
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
			//Loans
			Loan loan1 = new Loan("Mortage",800000.0, List.of( 12,24,36,48,60));
			Loan loan2 = new Loan("Personal",100000.0,List.of( 12,24,36,48,60));
			Loan loan3 = new Loan("Automotive",300000.0,List.of( 12,24,36,48,60));

			//ClientLoan
			ClientLoan clientloan1 = new ClientLoan(clientOne,loan1,400000.0,60);
			ClientLoan clientloan2 = new ClientLoan(clientOne,loan2,400000.0,12);
			ClientLoan clientloan3 = new ClientLoan(clientTwo,loan2,100000.0,24);
			ClientLoan clientloan4 = new ClientLoan(clientTwo,loan2,200000.0,36);

			//Cards
			Card card1 = new Card("1513-4546-5464-4645","555",CardType.DEBIT,CardColor.GOLD,LocalDate.now(),LocalDate.now());

			clientOne.addAccount(accOne);
			clientOne.addAccount(accTwo);
			clientTwo.addAccount(accTri);
			clientTwo.addAccount(accFour);
			clientOne.addLoan(clientloan1);
			clientOne.addLoan(clientloan2);
			clientTwo.addLoan(clientloan3);
			clientTwo.addLoan(clientloan4);
			accOne.addTransaction(t1);
			accOne.addTransaction(t2);
			accTwo.addTransaction(t3);
			accTwo.addTransaction(t4);
			accTri.addTransaction(t5);
			accTri.addTransaction(t6);
			accFour.addTransaction(t7);
			accFour.addTransaction(t8);
			loan1.addClient(clientloan1);
			loan2.addClient(clientloan2);
			loan2.addClient(clientloan3);
			loan2.addClient(clientloan4);

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
			loanrepository.save(loan1);
			loanrepository.save(loan2);
			loanrepository.save(loan3);
			clientloanrepository.save(clientloan1);
			clientloanrepository.save(clientloan2);
			clientloanrepository.save(clientloan3);
			clientloanrepository.save(clientloan4);
			cardrepository.save(card1);
		};
	}
}

package br.com.alura.screenmatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.screenmatch.principal.Principal;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	private static final Logger LOGGER = LogManager.getLogger();

	private Principal principal;

	ScreenmatchApplication(Principal principal) {
		this.principal = principal;
	}

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.exibeMenu();
	} 

}

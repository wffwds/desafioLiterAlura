package br.com.ffwds.desafioLiterAlura;

import br.com.ffwds.desafioLiterAlura.principal.Principal;
import br.com.ffwds.desafioLiterAlura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioLiterAluraApplication  implements CommandLineRunner {
	@Autowired
	private AutorRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(DesafioLiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.exibeMenu();
	}
}

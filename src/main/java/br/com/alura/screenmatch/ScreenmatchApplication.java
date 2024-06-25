package br.com.alura.screenmatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.IConversorDados;

@SpringBootApplication
public class ScreenmatchApplication {
	private static final Logger LOGGER = LogManager.getLogger();

	@Value("${api.key}")
	private String apiKey;

	private final ConsumoApi consumoApi;

	private final IConversorDados<DadosSerie> conversorDados;

	public ScreenmatchApplication(ConsumoApi consumoApi, @Lazy IConversorDados<DadosSerie> conversorDados) {
		this.consumoApi = consumoApi;
		this.conversorDados = conversorDados;
	}

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Bean
	ConsumoApi getConsumoApi() {
		return new ConsumoApi();
	}

	@Bean
	@Primary
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

    @Bean
    CommandLineRunner consumirDados() {
		String endereco = String.format("https://www.omdbapi.com/?t=gilmore+girls&apikey=%s", apiKey);
		
		LOGGER.info("Requisitando: {}", endereco);
		return args -> {
			String dados = consumoApi.obterDados(endereco);
			LOGGER.info("Resposta via CommandLineRunner: {}", dados);
			DadosSerie dadosSerie = conversorDados.obterDados(dados, DadosSerie.class);
			LOGGER.info("Dados da Serie: \n Title: {}, \nAvaliacao:  {}, \nTotal de Temporadas: {}", dadosSerie.title(), dadosSerie.avaliacao(), dadosSerie.totalTemporadas());
		};
	} 

}

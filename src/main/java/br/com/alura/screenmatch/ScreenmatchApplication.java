package br.com.alura.screenmatch;

import java.util.ArrayList;
import java.util.List;

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

import br.com.alura.screenmatch.model.DadosEpisodios;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.IConversorDados;

@SpringBootApplication
public class ScreenmatchApplication {
	private static final Logger LOGGER = LogManager.getLogger();

	@Value("${api.key}")
	private String apiKey;

	private final ConsumoApi consumoApi;

	private final IConversorDados<DadosSerie> conversorDados;
	private final IConversorDados<DadosEpisodios> conversorDadosEpisodios;
	private final IConversorDados<DadosTemporada> conversorDadosTemporada;

	public ScreenmatchApplication(ConsumoApi consumoApi, @Lazy IConversorDados<DadosSerie> conversorDados, @Lazy IConversorDados<DadosEpisodios> conversorDadosEpisodios, @Lazy IConversorDados<DadosTemporada> conversorDadosTemporada) {
		this.consumoApi = consumoApi;
		this.conversorDados = conversorDados;
		this.conversorDadosEpisodios = conversorDadosEpisodios;
		this.conversorDadosTemporada = conversorDadosTemporada;
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
		String serie = "gilmore girls".replaceAll(" ", "%20");
		String endereco = String.format("https://www.omdbapi.com/?t=%s&apikey=%s", serie, apiKey);
		
		LOGGER.info("Requisitando: {}", endereco);
		return args -> {
			List<DadosTemporada> listaDadosTemporada = new ArrayList<>();
			String dados = consumoApi.obterDados(endereco);
			LOGGER.info("Resposta via CommandLineRunner: {}", dados);
			DadosSerie dadosSerie = conversorDados.obterDados(dados, DadosSerie.class);

			String dadosEpisodios = consumoApi.obterDados(String.format("https://www.omdbapi.com/?t=%s&season=1&episode=2&apikey=%s", serie, apiKey));
			LOGGER.info("Requisitando dados de episodios: {}", dadosEpisodios);
			DadosEpisodios dadosEpisodiosObjeto = conversorDadosEpisodios.obterDados(dadosEpisodios, DadosEpisodios.class);
			
			LOGGER.info("Dados da Serie: \n Title: {}, \nAvaliacao:  {}, \nTotal de Temporadas: {}", dadosSerie.title(), dadosSerie.avaliacao(), dadosSerie.totalTemporadas());
			LOGGER.info("Dados Episodios: \n Title: {}, \nNumero:  {}, \n Avaliacao: {}, \nLançamento: {}", dadosEpisodiosObjeto.titulo(), dadosEpisodiosObjeto.numero(), dadosEpisodiosObjeto.avaliacao(), dadosEpisodiosObjeto.dataLancamento());
			
			for (int temporada = 1; temporada <= dadosSerie.totalTemporadas(); temporada++) {
				String dadosTemporada = consumoApi.obterDados(String.format("https://www.omdbapi.com/?t=%s&season=%d&apikey=%s", serie, temporada, apiKey));
				LOGGER.info("Requisitando dados da temporada: {}", dadosTemporada);
				DadosTemporada dadosTemporadaObjeto = conversorDadosTemporada.obterDados(dadosTemporada, DadosTemporada.class);
				LOGGER.info("Dados Temporada: \nNumero:  {}, \n Episodios: {}", dadosTemporadaObjeto.numeroTemporada(), dadosTemporadaObjeto.dadosEpisodios());

				listaDadosTemporada.add(dadosTemporadaObjeto);
			}

			listaDadosTemporada.forEach(t -> LOGGER.info(t.toString()));
		};
	} 

}

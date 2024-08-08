package br.com.alura.screenmatch.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsultaChatGPT;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.IConversorDados;

@Service
public class Principal {
    private static final Logger LOGGER = LogManager.getLogger();
    private Scanner scanner = new Scanner(System.in);

    private ConsumoApi consumoApi;
    private static final String ENDERECO = "https://www.omdbapi.com/?t=";

    @Value("${api.key}")
    private String apiKey;

    private final IConversorDados<DadosSerie> conversorDadosSeries;
    private final IConversorDados<DadosTemporada> conversorDadosTemporada;

    public Principal(ConsumoApi consumoApi, @Lazy IConversorDados<DadosSerie> conversorDadosSeries,
            @Lazy IConversorDados<DadosTemporada> conversorDadosTemporada) {
        this.consumoApi = consumoApi;
        this.conversorDadosSeries = conversorDadosSeries;
        this.conversorDadosTemporada = conversorDadosTemporada;
    }

    public void exibeMenu() {
        var menu = """
                1 - Buscar séries
                2 - Buscar episódios

                0 - Sair
                """;

        System.out.println(menu);
        var opcao = Integer.parseInt(scanner.nextLine());

        final int opcaoSerie = 1;
        final int opcaoEpisodio = 2;
        final int opcaoSaida = 0;

        switch (opcao) {
            case opcaoSerie -> buscarSerieWeb();
            case opcaoEpisodio -> buscarEpisodioPorSerie();
            case opcaoSaida -> System.out.println("Saindo...");
            default -> System.out.println("Opção Inválida: " + opcao);
        }

    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = scanner.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&apikey=" + apiKey);
        DadosSerie dados = null;
        try {
            dados = conversorDadosSeries.obterDados(json, DadosSerie.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Falha ao Processar o JSON: ", e);
        }
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumoApi
                    .obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + "&apikey=" + apiKey);
            DadosTemporada dadosTemporada;
            try {
                dadosTemporada = conversorDadosTemporada.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            } catch (JsonProcessingException e) {
                LOGGER.error("Falha ao Processar o JSON: ", e);
            }

        }

        temporadas.forEach(System.out::println);
    }

}

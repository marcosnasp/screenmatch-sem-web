package br.com.alura.screenmatch.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.Timeout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConsumoApi {

    @Value("${proxy.host}")
    public String proxyHost;

    @Value("${proxy.port}")
    public String proxyPort;

    private static final Logger LOGGER = LogManager.getLogger();

    public String obterDados(String endereco) {
        LOGGER.info("Obtendo dados de: {}", endereco);
        LOGGER.info("Obtendo via proxy: {}:{}", proxyHost, proxyPort);
        
        String enderecoProxy = String.format("%s:%s", proxyHost, proxyPort);
        String respostaRequisicao = null;

        try {
            HttpHost proxy = HttpHost.create(enderecoProxy);
            respostaRequisicao = Request.get(endereco)
                    .connectTimeout(Timeout.of(10, TimeUnit.SECONDS))
                    .responseTimeout(Timeout.of(10, TimeUnit.SECONDS))
                    .viaProxy(proxy)
                    .execute()
                    .returnContent()
                    .asString();

            LOGGER.info("Resposta: {}", respostaRequisicao);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Proxy inválido: " + e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.debug(e.getMessage(), e);
        } catch (URISyntaxException e) {
            LOGGER.debug(e.getMessage(), e);
        }
        return respostaRequisicao;
    }
}

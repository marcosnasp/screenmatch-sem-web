package br.com.alura.screenmatch.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConversorDados<T> implements IConversorDados<T> {

    //private static final Logger LOGGER = LogManager.getLogger();

    private final ObjectMapper objectMapper;

    public ConversorDados(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public T obterDados(String dados, Class<T> classe) throws JsonProcessingException {
        try {
            return objectMapper.readValue(dados, classe);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }


}

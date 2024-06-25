package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IConversorDados<T> {
    T obterDados(String dados, Class<T> classe) throws JsonProcessingException;
}

package br.com.alura.screenmatch.service;

import org.springframework.beans.factory.annotation.Value;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {

    @Value("${api.key.openai}")
    private final String apiChatGptKey;

    public ConsultaChatGPT(String apiChatGptKey) {
        this.apiChatGptKey = apiChatGptKey;
    }

    public String obterTraducao(String texto) {
        OpenAiService service = new OpenAiService(apiChatGptKey);

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
}
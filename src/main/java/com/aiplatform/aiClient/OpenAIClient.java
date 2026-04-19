package com.aiplatform.aiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenAIClient implements AIClient {

	private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    public OpenAIClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String analyze(String prompt) {

    	Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("model", "openai/gpt-3.5-turbo");

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content",
                "You are a support ticket classifier. " +
                "Return ONLY valid JSON with fields: category, sentiment, priority. " +
                "Do not include explanations.");
        
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        requestBody.put("messages", List.of(systemMessage, userMessage));
        requestBody.put("temperature", 0.2);

        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
}
}

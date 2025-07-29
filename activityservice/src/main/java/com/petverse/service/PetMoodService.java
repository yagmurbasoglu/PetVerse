package com.petverse.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value; // bu önemli
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class PetMoodService {

    private final WebClient client;

    // Hugging Face token'ınızı https://huggingface.co/settings/tokens adresinden alıp application.yml dosyasına ekleyin
    public PetMoodService(@Value("${huggingface.token}") String hfToken) {
        this.client = WebClient.builder()
                .baseUrl("https://api-inference.huggingface.co")
                .defaultHeader("Authorization", "Bearer " + hfToken)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String predictMood(String sentence) {
        Map<String, String> request = Map.of("inputs", sentence);

        String response = client.post()
                .uri("/models/j-hartmann/emotion-english-distilroberta-base")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.get(0).get(0).get("label").asText();
        } catch (Exception e) {
            return "neutral";
        }
    }
}

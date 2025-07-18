package com.petverse.apigateway.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import org.springframework.context.annotation.Primary;


import java.util.Base64;

@Configuration
public class RateLimiterConfig {

    // Her IP adresi için ayrı rate limit uygular
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress()
        );
    }
    // Her kullanıcı (JWT içindeki userId) için ayrı rate limit uygular
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String[] chunks = token.split("\\.");
                if (chunks.length > 1) {
                    String payload = new String(Base64.getDecoder().decode(chunks[1]));
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode node = mapper.readTree(payload);
                        return Mono.just(node.get("userId").asText());
                    } catch (Exception e) {
                        return Mono.just("anonymous");
                    }
                }
            }
            return Mono.just("anonymous");
        };
    }
}

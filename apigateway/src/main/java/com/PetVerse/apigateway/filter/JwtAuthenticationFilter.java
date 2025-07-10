package com.petverse.apigateway.filter;

import com.petverse.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Component
@Order(0)
public class JwtAuthenticationFilter implements GlobalFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Request path: " + path);  // DEBUG log

        // /auth içeren endpoint'ler token istemesin
        if (path.contains("/auth")) {
            return chain.filter(exchange);
        }

        // Authorization header kontrolü
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Token alınıyor
        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.validateToken(token);
            // X-User-Email header eklemesi (downstream servislerde kullanılabilir)
            exchange = exchange.mutate()
                    .request(builder -> builder.header("X-User-Email", claims.getSubject()))
                    .build();
        } catch (JwtException e) {
            // JWT parsing veya doğrulama hatası
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } catch (Exception e) {
            // Her ihtimale karşı genel hata
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}

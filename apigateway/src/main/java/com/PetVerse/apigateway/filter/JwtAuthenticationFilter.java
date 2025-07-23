package com.PetVerse.apigateway.filter;

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


        if (path.contains("/api/auth") ||
            path.contains("/auth") ||
            path.contains("/v3/api-docs") ||
            path.contains("/swagger-ui") ||
            path.contains("/swagger-resources") ||
            path.contains("/webjars") ||
            path.contains("/api-docs")) {
            return chain.filter(exchange);
        }

        //Authorization header kontrolü
        //Header yoksa veya Bearer ile başlamıyorsa 401 UNAUTHORIZED döner.
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.validateToken(token);

            // Header'a user bilgileri ekle (id veya email)
            String email = claims.getSubject();
            String userId = claims.get("userId", String.class); // Eğer token içinde varsa

            //Token’dan çıkan email ve userId bilgilerini header olarak ekler.
            //Böylece arkadaki mikroservisler bu bilgileri JWT çözmeden kullanabilir.
            exchange = exchange.mutate()
                    .request(builder -> builder
                        .header("X-User-Email", email)
                        .header("X-User-Id", userId != null ? userId : ""))
                    .build();

                           
        } catch (JwtException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}


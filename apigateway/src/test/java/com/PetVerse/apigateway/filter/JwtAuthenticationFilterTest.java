package com.petverse.apigateway.filter;

import com.petverse.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private GatewayFilterChain chain;

    @Mock
    private Claims claims;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAllowRequestWithValidToken() {
        // arrange
        String token = "valid.token.value";
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/pets")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();

        ServerWebExchange exchange = MockServerWebExchange.from(request);

        when(jwtUtil.validateToken(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("test@example.com");
        when(claims.get("userId", String.class)).thenReturn("42");

        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        // act
        Mono<Void> result = filter.filter(exchange, chain);

        // assert
        verify(chain).filter(any(ServerWebExchange.class));
    }

    @Test
    void shouldRejectRequestWithoutAuthorizationHeader() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/pets").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // act
        filter.filter(exchange, chain).block();

        // assert
        ServerHttpResponse response = exchange.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldRejectInvalidToken() {
        String token = "invalid.token";
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/pets")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();

        ServerWebExchange exchange = MockServerWebExchange.from(request);

        when(jwtUtil.validateToken(token)).thenThrow(new JwtException("Invalid token"));

        // act
        filter.filter(exchange, chain).block();

        // assert
        ServerHttpResponse response = exchange.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}

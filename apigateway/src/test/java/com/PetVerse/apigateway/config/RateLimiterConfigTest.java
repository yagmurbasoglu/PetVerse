package com.petverse.apigateway.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.springframework.test.context.ActiveProfiles;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class RateLimiterConfigTest {

    private RateLimiterConfig config;

    @BeforeEach
    void setUp() {
        config = new RateLimiterConfig();
    }

    @Test
    void shouldResolveIpKey() {
        var request = MockServerHttpRequest.get("/test")
                .remoteAddress(new InetSocketAddress("192.168.1.100", 8080))
                .build();
        var exchange = MockServerWebExchange.from(request);

        Mono<String> keyMono = config.ipKeyResolver().resolve(exchange);

        StepVerifier.create(keyMono)
                .expectNext("192.168.1.100")
                .verifyComplete();
    }

    @Test
    void shouldResolveUserKey_fromValidJwt() {
        String validJwt = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9."
                + "eyJ1c2VySWQiOiIxMjMiLCJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSJ9."
                + "signature-placeholder";  // signature önemli değil, parsing için yeterli

        var request = MockServerHttpRequest.get("/test")
                .header("Authorization", validJwt)
                .build();
        var exchange = MockServerWebExchange.from(request);

        Mono<String> keyMono = config.userKeyResolver().resolve(exchange);

        StepVerifier.create(keyMono)
                .expectNext("123")
                .verifyComplete();
    }

    @Test
    void shouldFallbackToAnonymous_whenJwtIsMissingOrInvalid() {
        var request = MockServerHttpRequest.get("/test").build();
        var exchange = MockServerWebExchange.from(request);

        Mono<String> keyMono = config.userKeyResolver().resolve(exchange);

        StepVerifier.create(keyMono)
                .expectNext("anonymous")
                .verifyComplete();
    }
}

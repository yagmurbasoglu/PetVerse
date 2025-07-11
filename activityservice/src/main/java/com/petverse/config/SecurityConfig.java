package com.petverse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html", "/actuator/**" , "/**").permitAll() //Tüm endpointlere erişim serbest
                .anyRequest().permitAll()
            );
        return http.build();
    }
}

package com.petverse.userservice.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.annotations.RouterOperation;


@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("UserService API")
                .version("1.0")
                .description("PetVerse - UserService Swagger UI")
                .contact(new Contact().name("Yağmur Başoğlu").email("yagmur@example.com")));
    }
}

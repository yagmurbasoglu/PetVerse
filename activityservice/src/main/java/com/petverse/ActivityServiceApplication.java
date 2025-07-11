package com.petverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Import;
import com.petverse.OpenAPIConfig; // ✅ OpenAPIConfig'i zorla dahil et

@SpringBootApplication
@EnableRabbit
@EntityScan(basePackages = "com.petverse.model")
@EnableJpaRepositories(basePackages = "com.petverse.repository")
@Import(OpenAPIConfig.class)  // ✅ OpenAPIConfig'i zorla dahil et
public class ActivityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivityServiceApplication.class, args);
    }
}
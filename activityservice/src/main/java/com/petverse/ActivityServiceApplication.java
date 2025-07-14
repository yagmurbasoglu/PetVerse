package com.petverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableRabbit
@EnableDiscoveryClient
@EntityScan(basePackages = "com.petverse.model")
@EnableJpaRepositories(basePackages = "com.petverse.repository")
public class ActivityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivityServiceApplication.class, args);
    }
}
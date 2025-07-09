package com.petservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.petservice")
@EnableJpaRepositories(basePackages = "com.petservice.repository")
public class PetserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetserviceApplication.class, args);
    }
}

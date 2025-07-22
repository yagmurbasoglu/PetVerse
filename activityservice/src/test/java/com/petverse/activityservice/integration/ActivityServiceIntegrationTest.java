package com.petverse.integration;

import com.petverse.model.Activity;
import com.petverse.repository.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.petverse.model.ActivityType;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class ActivityServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ActivityRepository repository;

    @Test
    void shouldPersistAndReadActivity() {
        Activity activity = Activity.builder()
                .type(ActivityType.FEED)
                .description("Mama verildi")
                .userId(1L)
                .petId(2L)
                .build();

        repository.save(activity);

        List<Activity> activities = repository.findAll();

        assertThat(activities).isNotEmpty();
        assertThat(activities.get(0).getDescription()).isEqualTo("Mama verildi");
    }
}

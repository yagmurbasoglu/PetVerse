package com.petverse.integration;

import com.petverse.model.Activity;
import com.petverse.repository.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = ActivityServiceIntegrationTest.DockerPostgresInitializer.class)
public class ActivityServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    static class DockerPostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            ).applyTo(context.getEnvironment());
        }
    }

    @Autowired
    private ActivityRepository repository;

    @Test
    void shouldPersistAndReadActivity() {
        // arrange
        Activity activity = Activity.builder()
                .type("feed")
                .description("Mama verildi")
                .userId(1L)
                .petId(2L)
                .build();

        repository.save(activity);

        // act
        List<Activity> activities = repository.findAll();

        // assert
        assertThat(activities).isNotEmpty();
        assertThat(activities.get(0).getDescription()).isEqualTo("Mama verildi");
    }
}

package com.petverse.notificationservice.integration;

import com.petverse.notificationservice.model.Notification;
import com.petverse.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotificationServiceIntegrationTest {

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
    private NotificationRepository repository;

    @Test
    @Order(1)
    void shouldSaveNotificationToPostgres() {
        Notification notif = Notification.builder()
                .userId("test-user-id")
                .type("info")
                .message("Test mesajı")
                .timestamp(LocalDateTime.now())
                .build();

        Notification saved = repository.save(notif);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo("test-user-id");
    }

    @Test
    @Order(2)
    void shouldRetrieveNotificationByUserId() {
        List<Notification> results = repository.findByUserIdOrderByTimestampDesc("test-user-id");

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getMessage()).isEqualTo("Test mesajı");
    }
}

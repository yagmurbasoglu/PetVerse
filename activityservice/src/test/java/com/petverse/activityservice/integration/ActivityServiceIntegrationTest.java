package com.petverse.integration;

import com.petverse.model.Activity;
import com.petverse.model.ActivityType;
import com.petverse.repository.ActivityRepository;
import com.petverse.service.PetMoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create"
})
public class ActivityServiceIntegrationTest {

    @MockBean
    private PetMoodService petMoodService;

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

package com.petservice.integration;

import com.petservice.dto.PetDTO;
import com.petservice.model.Pet;
import com.petservice.repository.PetRepository;
import com.petservice.service.PetService;
import com.petservice.service.impl.PetServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // <-- Bu EKLENDİ
public class PetServiceIntegrationTest {

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
    private PetServiceImpl petService;

    @Autowired
    private PetRepository petRepository;

    private static Long petId;

    @Test
    @Order(1)
    void shouldSavePetToRealPostgres() {
        PetDTO dto = new PetDTO(null, "Testy", "Dog", 3, 100L);
        PetDTO saved = petService.createPet(dto);
        petId = saved.getId();

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Testy");
        assertThat(saved.getUserId()).isEqualTo(100L);
    }

    @Test
    @Order(2)
    void shouldFetchPetByUserId() {
        List<PetDTO> pets = petService.getPetsByUserId("100");
        assertThat(pets).isNotEmpty();
        assertThat(pets.get(0).getName()).isEqualTo("Testy");
    }

    @Test
    @Order(3)
    void shouldDeletePetIfUserOwnsIt() {
        petService.deletePet(petId, 100L);
        boolean exists = petRepository.findById(petId).isPresent();
        assertThat(exists).isFalse();
    }
}

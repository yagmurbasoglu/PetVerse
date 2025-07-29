package com.petverse.userservice.integration;

import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.model.User;
import com.petverse.userservice.repository.UserRepository;
import com.petverse.userservice.service.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import com.petverse.userservice.security.JwtService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
  properties = {
    "spring.config.import=",
    "spring.datasource.url=jdbc:tc:postgresql:15:///testdb",
    "spring.datasource.username=test",
    "spring.datasource.password=test",
    "spring.jpa.hibernate.ddl-auto=create",
    "jwt.public.key=mocked",
    "jwt.private.key=mocked"
  }
)
@Testcontainers
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private JwtEncoder jwtEncoder;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private JwtService jwtService;

    @Test
    @Order(1)
    void shouldSaveUserToRealPostgres() {
        UserDto dto = new UserDto("test@example.com", "1234", "testuser");

        User saved = userService.registerUser(dto);

        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @Order(2)
    void shouldFindUserByEmail() {
        var user = userRepository.findByEmail("test@example.com");

        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo("test@example.com");
    }
}

package com.petverse.userservice.service;

import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.model.User;
import com.petverse.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        UserDto userDto = new UserDto("test@example.com", "1234", "testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("hashedpassword");

        User savedUser = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password("hashedpassword")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.registerUser(userDto);

        // Assert
        assertEquals("test@example.com", result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserDto userDto = new UserDto("test@example.com", "1234", "testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(new User()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.registerUser(userDto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("e-posta"));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        UserDto userDto = new UserDto("test@example.com", "1234", "testuser");

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(new User()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.registerUser(userDto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("kullanıcı adı"));
    }
}

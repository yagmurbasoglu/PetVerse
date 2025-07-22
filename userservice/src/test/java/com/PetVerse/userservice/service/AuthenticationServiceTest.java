package com.petverse.userservice.service;

import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.model.User;
import com.petverse.userservice.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

@Test
void shouldReturnTokenOnSuccessfulLogin() {
    // Arrange
    UserDto userDto = new UserDto("test@example.com", "1234", "testuser");

    Authentication auth = mock(Authentication.class);
    UserDetails userDetails = mock(UserDetails.class);

    when(authenticationManager.authenticate(any())).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn("test@example.com"); // email dönüyor

    User user = User.builder()
            .id(1L)
            .email("test@example.com")
            .username("testuser")
            .build();

    when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
    when(jwtService.generateToken("test@example.com", "1")).thenReturn("mocked-token");

    // Act
    String token = authenticationService.login(userDto);

    // Assert
    assertEquals("mocked-token", token);
    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
}


@Test
void shouldThrowExceptionIfUserNotFound() {
    // Arrange
    UserDto userDto = new UserDto("notfound@example.com", "1234", "any");

    Authentication auth = mock(Authentication.class);
    UserDetails userDetails = mock(UserDetails.class);

    when(authenticationManager.authenticate(any())).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn("notfound@example.com"); // email dönüyor

    when(userService.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException ex = assertThrows(RuntimeException.class,
        () -> authenticationService.login(userDto)
    );
    assertEquals("User not found", ex.getMessage());
}



}

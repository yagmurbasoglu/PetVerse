package com.petverse.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.security.JwtService;
import com.petverse.userservice.service.AuthenticationService;
import com.petverse.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.petverse.userservice.model.User;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void shouldLoginSuccessfully() throws Exception {
        UserDto loginDto = new UserDto("test@example.com", "1234", "testuser");

        when(authenticationService.login(loginDto)).thenReturn("mocked-token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked-token"));
    }

@Test
void shouldReturnUnauthorizedWhenLoginFails() throws Exception {
    UserDto loginDto = new UserDto("notfound@example.com", "wrongpass", "any");

    when(authenticationService.login(loginDto))
            .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginDto)))
            .andExpect(status().isUnauthorized())
            .andExpect(status().reason("User not found"));
}

@Test
void shouldRegisterUserSuccessfully() throws Exception {
    UserDto registerDto = new UserDto("test@example.com", "1234", "testuser");

    User savedUser = User.builder()
            .id(1L)
            .email("test@example.com")
            .username("testuser")
            .password("hashed-password")
            .build();

    when(userService.registerUser(registerDto)).thenReturn(savedUser);
    when(jwtService.generateToken("test@example.com", "1")).thenReturn("mocked-token");

    mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerDto)))
            .andExpect(status().isOk())
            .andExpect(content().string("mocked-token"));
}



}

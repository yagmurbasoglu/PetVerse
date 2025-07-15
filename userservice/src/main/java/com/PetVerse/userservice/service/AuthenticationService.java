package com.petverse.userservice.service;

import com.petverse.userservice.model.User;

import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService; // 🔥 bunu ekle

public String login(UserDto userDto) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
    );

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    // Email'den kullanıcıyı çek
    User user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Yeni token: username + userId
    return jwtService.generateToken(user.getEmail(), user.getId().toString());
}

}
package com.petverse.userservice.controller;

import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.model.User;
import com.petverse.userservice.service.AuthenticationService;
import com.petverse.userservice.service.UserService;
import com.petverse.userservice.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        logger.info("Login denemesi: {}", userDto.getEmail());
        String token = authenticationService.login(userDto);
        logger.info("Login başarılı: {}", userDto.getEmail());
        return ResponseEntity.ok(token);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        User savedUser = userService.registerUser(userDto);

        String jwtToken = jwtService.generateToken(
            savedUser.getUsername(),
            savedUser.getId().toString()
        );

        return ResponseEntity.ok(jwtToken);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Long> getUserIdByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(user.getId());
    }

}
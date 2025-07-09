package com.petverse.userservice.controller;

import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.model.User;
import com.petverse.userservice.repository.UserRepository;
import com.petverse.userservice.security.JwtUtil;
import com.petverse.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth") // Bu controller sadece login/register işlemleri için
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;

        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        System.out.println(">>> register endpoint'e istek geldi: " + userDto.getEmail());
        
        User savedUser = userService.registerUser(userDto);

        String token = jwtUtil.generateToken(savedUser.getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", savedUser.getId(),
                "email", savedUser.getEmail()
        ));
        }

    // 🔐 LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        try {
            // Kimlik doğrulama
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getEmail(),  // email ile giriş yapıyoruz
                            userDto.getPassword()
                    )
            );

            // Kullanıcıyı veri tabanından getir
            User user = userRepository.findByEmail(userDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            // JWT oluştur (kullanıcının e-postasını username olarak kullanıyoruz)
            String token = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "userId", user.getId(),
                    "email", user.getEmail()
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", "Giriş başarısız: e-posta veya şifre hatalı"));
        }
    }



}

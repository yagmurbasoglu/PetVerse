package com.PetVerse.userservice.controller;

import com.PetVerse.userservice.dto.UserDto;
import com.PetVerse.userservice.model.User;
import com.PetVerse.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        User savedUser = userService.registerUser(userDto);
        return ResponseEntity.ok(savedUser);
    }
}

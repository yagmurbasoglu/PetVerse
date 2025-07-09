package com.petverse.userservice.controller;

import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.model.User;
import com.petverse.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        User savedUser = userService.registerUser(userDto);
        return ResponseEntity.ok(new UserDto(savedUser)); // User → UserDto
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestAttribute("user") User user) {
        return ResponseEntity.ok(new UserDto(user));
    }
}
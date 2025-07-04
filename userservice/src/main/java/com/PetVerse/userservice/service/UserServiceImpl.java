package com.PetVerse.userservice.service;

import com.PetVerse.userservice.dto.UserDto;
import com.PetVerse.userservice.model.User;
import com.PetVerse.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User registerUser(UserDto userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword()) // TODO: Add hashing later
                .build();
        return userRepository.save(user);
    }
}

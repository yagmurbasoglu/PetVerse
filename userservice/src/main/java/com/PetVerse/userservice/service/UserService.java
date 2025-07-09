package com.petverse.userservice.service;

import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.model.User;
import java.util.Optional;

public interface UserService {
    User registerUser(UserDto userDto);
    Optional<User> findByEmail(String email);
}


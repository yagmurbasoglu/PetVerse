package com.PetVerse.userservice.service;

import com.PetVerse.userservice.dto.UserDto;
import com.PetVerse.userservice.model.User;

public interface UserService {
    User registerUser(UserDto userDto);
}

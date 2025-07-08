package com.petverse.userservice.service;

import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.model.User;

public interface UserService {
    User registerUser(UserDto userDto);
}

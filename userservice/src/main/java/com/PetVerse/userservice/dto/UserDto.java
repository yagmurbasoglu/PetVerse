package com.petverse.userservice.dto;

import com.petverse.userservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String email;
    private String password;
    private String username;

    public UserDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = "";
    }
}
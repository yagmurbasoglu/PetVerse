package com.petverse.userservice.dto;

import com.petverse.userservice.model.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String email;
    private String password;
    private String fullName;
    private String username;

    public String getUsername() {
    return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserDto(User user) {
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.password = ""; // Şifre DTO'da tutulmaz, güvenlik için boş bırakılır
    }
}

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
    private String password; // sadece girişte kullanılır
    private String username;

    // Kullanıcıdan DTO üretmek için constructor
    public UserDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = ""; // Güvenlik gereği şifre asla dışarı verilmez
    }
}

package com.petverse.userservice.service;

import com.petverse.userservice.dto.UserDto;
import com.petverse.userservice.model.User;
import com.petverse.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserDto userDto) {
        System.out.println(">>> registerUser metodu çağrıldı: " + userDto.getEmail());

        User user = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        System.out.println(">>> Kullanıcı başarıyla kaydedildi: ID = " + savedUser.getId());

        return savedUser;
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}

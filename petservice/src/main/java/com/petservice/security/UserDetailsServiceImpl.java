package com.petservice.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Şifre kontrolü yapmıyoruz, çünkü bu servis sadece token'ı doğruluyor
        return User.builder()
                .username(username)
                .password("") // boş bırakılabilir
                .authorities(Collections.emptyList()) // roller boş bırakılabilir
                .build();
    }
}

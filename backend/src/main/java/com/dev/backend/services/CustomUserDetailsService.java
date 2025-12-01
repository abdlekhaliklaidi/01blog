package com.dev.backend.services;

import com.dev.backend.entities.User;
import com.dev.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String role = "admin@gmail.com".equalsIgnoreCase(email) ? "ADMIN" : "USER";

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(role)
                .build();
    }

    public User findByEmail(String email) {
    return userRepository.findByEmail(email)
                         .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
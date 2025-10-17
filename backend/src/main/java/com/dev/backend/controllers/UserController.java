package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.dev.backend.entities.User;
import com.dev.backend.repositories.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getUsers() {
        System.out.println("Fetching all users...");
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body("Email is already registered.");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User savedUser = userRepository.save(user);
    return ResponseEntity.ok(savedUser);
    }

    
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User loginRequest) {
    return userRepository.findByEmail(loginRequest.getEmail())
        .map(user -> {
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }
    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
            .map(user -> {
                user.setFirstname(updatedUser.getFirstname());
                user.setLastname(updatedUser.getLastname());
                user.setGenre(updatedUser.getGenre());
                user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
                return userRepository.save(user);
            })
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}

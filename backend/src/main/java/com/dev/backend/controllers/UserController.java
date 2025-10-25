package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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
}

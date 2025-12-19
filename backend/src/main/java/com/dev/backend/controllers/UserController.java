package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.dev.backend.util.JwtUtil;
import com.dev.backend.dto.UserDTO;
import java.util.stream.Collectors;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // @GetMapping
    // public List<User> getUsers() {
    //     return userRepository.findAll();
    // }

    @GetMapping
    public List<UserDTO> getUsers() {
    List<User> users = userRepository.findAll();
    return users.stream()
                .map(u -> new UserDTO(u.getId(), u.getFirstname(), u.getLastname(), u.getEmail(), u.getAvatar()))
                .collect(Collectors.toList());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
    String token = authHeader.substring(7);

    String email = jwtUtil.extractEmail(token);
    User user = userRepository.findByEmail(email)
                              .orElseThrow(() -> new RuntimeException("User not found"));

    UserDTO dto = new UserDTO(user.getId(), user.getFirstname(), user.getLastname(),
                              user.getEmail(), user.getAvatar());
    return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
    UserDTO dto = new UserDTO(user.getId(), user.getFirstname(), user.getLastname(),
                              user.getEmail(), user.getAvatar());
    return ResponseEntity.ok(dto);
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

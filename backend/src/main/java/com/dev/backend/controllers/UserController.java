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
import jakarta.validation.Valid;
import com.dev.backend.dto.RegisterDTO;


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
                .filter(u -> !u.getEmail().equals("admin@gmail.com"))
                .map(u -> new UserDTO(u.getId(), u.getFirstname(), u.getLastname(), u.getEmail(), u.getAvatar(), u.getBio()))
                .collect(Collectors.toList());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
    String token = authHeader.substring(7);

    String email = jwtUtil.extractEmail(token);
    User user = userRepository.findByEmail(email)
                              .orElseThrow(() -> new RuntimeException("User not found"));

    UserDTO dto = new UserDTO(user.getId(), user.getFirstname(), user.getLastname(),
                              user.getEmail(), user.getAvatar(), user.getBio());
    return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
    UserDTO dto = new UserDTO(user.getId(), user.getFirstname(), user.getLastname(),
                              user.getEmail(), user.getAvatar(), user.getBio());
    return ResponseEntity.ok(dto);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
        @PathVariable Long id,
        @RequestBody User updatedUser) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    user.setFirstname(updatedUser.getFirstname());
    user.setLastname(updatedUser.getLastname());
    user.setBio(updatedUser.getBio());
    user.setAvatar(updatedUser.getAvatar());

    userRepository.save(user);

    return ResponseEntity.ok(
        new UserDTO(
            user.getId(),
            user.getFirstname(),
            user.getLastname(),
            user.getEmail(),
            user.getAvatar(),
            user.getBio()
        )
    );
}

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterDTO dto) {

    if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Cet email est déjà utilisé.");
    }

    User user = new User();
    user.setFirstname(dto.getFirstname());
    user.setLastname(dto.getLastname());
    user.setGenre(dto.getGenre());
    user.setEmail(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRole("USER");

    userRepository.save(user);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body("User registered successfully.");
    }

}

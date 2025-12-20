package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.dev.backend.util.JwtUtil;
import com.dev.backend.dto.LoginRequest;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.UserRepository;

import com.dev.backend.services.PostService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;


    @DeleteMapping("/delete-post/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

    @PostMapping("/ban-user/{userId}")
    public User banUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                      .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBanned(true);
        return userRepository.save(user);
    }
}

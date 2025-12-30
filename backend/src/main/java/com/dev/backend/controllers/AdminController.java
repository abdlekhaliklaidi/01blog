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
import com.dev.backend.dto.AdminUserDTO;

import com.dev.backend.services.PostService;
import com.dev.backend.services.ReportService;
import com.dev.backend.services.AdminService;
import com.dev.backend.dto.AdminPostDTO;
import com.dev.backend.entities.Post;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePostAsAdmin(@PathVariable Long id) {
    postService.deletePostAsAdmin(id);
    return ResponseEntity.noContent().build();
    }

    @PostMapping("/ban-user/{id}")
    public ResponseEntity<Void> banUser(@PathVariable Long id) {
    adminService.banUser(id);
    return ResponseEntity.ok().build();
    }

    @PostMapping("/unban-user/{id}")
    public ResponseEntity<Void> unbanUser(@PathVariable Long id) {
    adminService.unbanUser(id);
    return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
    return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/posts")
    public ResponseEntity<List<AdminPostDTO>> getAllPosts() {
    return ResponseEntity.ok(adminService.getAllPosts());
    }

    @PostMapping("/hide-post/{id}")
    public ResponseEntity<Void> hidePost(@PathVariable Long id) {
    adminService.hidePost(id);
    return ResponseEntity.ok().build();
    }

    @PostMapping("/unhide-post/{id}")
    public ResponseEntity<Void> unhidePost(@PathVariable Long id) {
    adminService.unhidePost(id);
    return ResponseEntity.ok().build();
}

}

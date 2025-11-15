package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;

import com.dev.backend.entities.Post;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.PostRepository;
import com.dev.backend.repositories.UserRepository;
import com.dev.backend.services.PostService;
import com.dev.backend.dto.PostDTO;
import java.util.stream.Collectors;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
// import java.util.Optional;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<PostDTO> getAllPosts() {
           return postService.getAllPosts().stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        return postService.getPostById(id)
            .map(post -> ResponseEntity.ok(new PostDTO(post)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/author/{authorId}")
    public List<PostDTO> getPostsByAuthor(@PathVariable Long authorId) {
       return postService.getPostsByAuthor(authorId)
        .stream()
        .map(PostDTO::new)
        .collect(Collectors.toList());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PostDTO> createPostWithImage(
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {

    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User author = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Post post = new Post();
    post.setTitle(title);
    post.setContent(content);
    post.setAuthor(author);

    if (imageFile != null && !imageFile.isEmpty()) {
        String base64 = Base64.getEncoder().encodeToString(imageFile.getBytes());
        post.setImageBase64(base64);
    }

    post.setCreatedAt(LocalDateTime.now());
    Post savedPost = postRepository.save(post);

    return ResponseEntity.status(HttpStatus.CREATED).body(new PostDTO(savedPost));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        try {
            Post post = postService.updatePost(id, updatedPost);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
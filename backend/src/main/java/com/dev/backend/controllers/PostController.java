package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/author/{authorId}")
    public List<Post> getPostsByAuthor(@PathVariable Long authorId) {
        return postService.getPostsByAuthor(authorId);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PostDTO> createPostWithImage(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("authorId") Long authorId,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        if (imageFile != null && !imageFile.isEmpty()) {
            String base64 = Base64.getEncoder().encodeToString(imageFile.getBytes());
           post.setImageBase64(base64);
        }

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);

        PostDTO postDTO = new PostDTO(savedPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(postDTO);
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
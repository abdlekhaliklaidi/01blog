package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;

import com.dev.backend.entities.Post;
import com.dev.backend.entities.User;
import com.dev.backend.entities.Notification;
import com.dev.backend.services.NotificationService;

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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import com.dev.backend.services.FollowerService;


@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowerService followerService;

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
    
    @GetMapping("/feed/{userId}")
    public List<PostDTO> getFeed(@PathVariable Long userId) {

    List<Long> followingIds = new java.util.ArrayList<>(
        followerService.getFollowingOfUser(userId)
            .stream()
            .map(f -> f.getFollowing().getId())
            .toList()
    );

    followingIds.add(userId);

    return postService.getPostsByAuthors(followingIds)
            .stream()
            .map(PostDTO::new)
            .collect(Collectors.toList());
}

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PostDTO> createPostWithMedia(
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam(value = "image", required = false) MultipartFile imageFile,
        @RequestParam(value = "video", required = false) MultipartFile videoFile
    ) throws IOException {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User author = userRepository.findByEmail(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Post post = new Post();
    post.setTitle(title);
    post.setContent(content);
    post.setAuthor(author);

    if (imageFile != null && !imageFile.isEmpty()) {
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get("uploads/images/" + fileName);

        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, imageFile.getBytes());

        post.setImagePath("/images/" + fileName);
    }

    if (videoFile != null && !videoFile.isEmpty()) {
        String fileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
        Path videoPath = Paths.get("uploads/videos/" + fileName);

        Files.createDirectories(videoPath.getParent());
        Files.write(videoPath, videoFile.getBytes());

        post.setVideoUrl("/videos/" + fileName);
    }

    Post savedPost = postRepository.save(post);

    return ResponseEntity.status(HttpStatus.CREATED).body(new PostDTO(savedPost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(
        @PathVariable Long id,
        @RequestBody Post updatedPost) {

    String email = SecurityContextHolder.getContext()
            .getAuthentication().getName();

    Post post = postService.updatePost(id, updatedPost, email);
    return ResponseEntity.ok(new PostDTO(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {

    String email = SecurityContextHolder.getContext()
            .getAuthentication().getName();

    postService.deletePost(id, email);
    return ResponseEntity.noContent().build();
    }

}

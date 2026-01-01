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
import java.util.ArrayList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;


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
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
    return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/author/{authorId}")
    public List<PostDTO> getPostsByAuthor(@PathVariable Long authorId) {
        return postService.getPostsByAuthor(authorId)
            .stream()
            .map(PostDTO::new)
            .collect(Collectors.toList());
    }
    
    @GetMapping("/feed/{userId}")
    public List<PostDTO> getFeed(
        @PathVariable Long userId,
        @RequestParam(required = false) Long lastPostId
    ) {

    List<Long> followingIds = new ArrayList<>(
        followerService.getFollowingOfUser(userId)
            .stream()
            .map(f -> f.getFollowing().getId())
            .toList()
    );

    followingIds.add(userId);

    return postService
            .getFeedPaginated(followingIds, lastPostId, 6)
            .stream()
            .map(PostDTO::new)
            .collect(Collectors.toList());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createPostWithMedia(
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam(value = "image", required = false) MultipartFile imageFile,
        @RequestParam(value = "video", required = false) MultipartFile videoFile
    ) throws IOException {
    
        try {
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

    Post savedPost = postService.createPost(post);

    return ResponseEntity.status(HttpStatus.CREATED).body(new PostDTO(savedPost));
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getMessage());
    }
}

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public class RateLimitException extends RuntimeException {
    public RateLimitException(String message) {
        super(message);
    }
}

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<PostDTO> updatePostWithMedia(
    @PathVariable Long id,
    @RequestParam("title") String title,
    @RequestParam("content") String content,
    @RequestParam(value = "image", required = false) MultipartFile imageFile,
    @RequestParam(value = "video", required = false) MultipartFile videoFile
    ) throws IOException {
    
    Post updatedPost = new Post();
    updatedPost.setTitle(title);
    updatedPost.setContent(content);

    if (imageFile != null && !imageFile.isEmpty()) {
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get("uploads/images/" + fileName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, imageFile.getBytes());
        updatedPost.setImagePath("/images/" + fileName);
    }

    if (videoFile != null && !videoFile.isEmpty()) {
        String fileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
        Path videoPath = Paths.get("uploads/videos/" + fileName);
        Files.createDirectories(videoPath.getParent());
        Files.write(videoPath, videoFile.getBytes());
        updatedPost.setVideoUrl("/videos/" + fileName);
    }

    Post post = postService.updatePost(id, updatedPost);
    return ResponseEntity.ok(new PostDTO(post));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
    postService.deletePost(id);
    return ResponseEntity.noContent().build();
    }

}

package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.dev.backend.entities.Comment;
import com.dev.backend.entities.Notification;
import com.dev.backend.entities.Post;
import com.dev.backend.entities.User;

import com.dev.backend.services.CommentService;
import com.dev.backend.services.NotificationService;

import com.dev.backend.repositories.UserRepository;
import com.dev.backend.repositories.PostRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        return commentService.getCommentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/post/{postId}")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    @GetMapping("/user/{userId}")
    public List<Comment> getCommentsByUser(@PathVariable Long userId) {
        return commentService.getCommentsByUser(userId);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {

    Post post = postRepository.findById(comment.getPost().getId())
            .orElseThrow(() -> new RuntimeException("Post not found"));

    User user = userRepository.findById(comment.getUser().getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    comment.setCreatedAt(LocalDateTime.now());
    comment.setUser(user);
    comment.setPost(post);

    Comment createdComment = commentService.createComment(comment);

    createdComment.setUser(user);

    Notification notif = new Notification();
    notif.setUser(post.getAuthor());
    notif.setMessage(user.getFirstname() + " commented on your post: " + post.getTitle());
    notif.setCreatedAt(LocalDateTime.now());
    notificationService.create(notif);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
}


    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        try {
            Comment comment = commentService.updateComment(id, updatedComment);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

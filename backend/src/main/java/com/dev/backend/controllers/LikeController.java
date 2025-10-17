package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.dev.backend.entities.Like;
import com.dev.backend.services.LikeService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping
    public List<Like> getAllLikes() {
        return likeService.getAllLikes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Like> getLike(@PathVariable Long id) {
        return likeService.getLikeById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Like> createLike(@RequestBody Like like) {
        Like createdLike = likeService.createLike(like);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLike);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
        likeService.deleteLike(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<Void> deleteLikeByUserAndPost(@PathVariable Long userId, @PathVariable Long postId) {
        likeService.deleteLikeByUserAndPost(userId, postId);
        return ResponseEntity.noContent().build();
    }
}
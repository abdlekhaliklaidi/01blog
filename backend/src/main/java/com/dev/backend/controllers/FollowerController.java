package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.dev.backend.entities.Follower;
import com.dev.backend.services.FollowerService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/followers")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @GetMapping
    public List<Follower> getAllFollowers() {
        return followerService.getAllFollowers();
    }

    @GetMapping("/following/{userId}")
    public List<Follower> getFollowing(@PathVariable Long userId) {
        return followerService.getFollowingOfUser(userId);
    }

    @GetMapping("/followers/{userId}")
    public List<Follower> getFollowers(@PathVariable Long userId) {
        return followerService.getFollowersOfUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Follower> getFollower(@PathVariable Long id) {
        return followerService.getFollowerById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Follower> followUser(@RequestBody Follower follower) {
        Follower createdFollower = followerService.followUser(follower);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFollower);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long id) {
        followerService.unfollowUser(id);
        return ResponseEntity.noContent().build();
    }
}
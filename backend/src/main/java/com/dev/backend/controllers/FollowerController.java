package com.dev.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.dev.backend.entities.Follower;
import com.dev.backend.services.FollowerService;
import com.dev.backend.repositories.UserRepository;
import com.dev.backend.dto.FollowerDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/followers")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Follower> getAllFollowers() {
        return followerService.getAllFollowers();
    }

    @GetMapping("/following/{userId}")
    public List<FollowerDTO> getFollowing(@PathVariable Long userId) {
        return followerService.getFollowingOfUser(userId)
                .stream()
                .map(f -> new FollowerDTO(f, userId, false)) 
                .collect(Collectors.toList());
    }

    @GetMapping("/followers/{userId}")
    public List<FollowerDTO> getFollowers(@PathVariable Long userId) {
        return followerService.getFollowersOfUser(userId)
                .stream()
                .map(f -> new FollowerDTO(f, userId, true))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> followUser(@RequestBody Follower follower) {
        if (!userRepository.existsById(follower.getFollower().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Follower user not found"));
        }

        if (!userRepository.existsById(follower.getFollowing().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Following user not found"));
        }

        Follower createdFollower = followerService.followUser(follower);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFollower);
    }

    @DeleteMapping("/{followerId}/{followingId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        Follower follower = followerService.findByFollowerIdAndFollowingId(followerId, followingId);
        if (follower == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Follower relationship not found"));
        }

        followerService.unfollowUser(followerId, followingId);
        return ResponseEntity.noContent().build();
    }
}

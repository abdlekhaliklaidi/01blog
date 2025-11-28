package com.dev.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.dev.backend.entities.Follower;
import com.dev.backend.repositories.FollowerRepository;
import com.dev.backend.repositories.UserRepository;
import com.dev.backend.entities.Notification;
import com.dev.backend.services.NotificationService;

import java.util.List;
import java.util.Optional;

@Service
public class FollowerService {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;


    public List<Follower> getAllFollowers() {
        return followerRepository.findAll();
    }

    public List<Follower> getFollowersOfUser(Long userId) {
        return followerRepository.findByFollowingId(userId);
    }

    public List<Follower> getFollowingOfUser(Long userId) {
        return followerRepository.findByFollowerId(userId);
    }

    public Optional<Follower> getFollowerById(Long id) {
        return followerRepository.findById(id);
    }

    public Follower followUser(Follower follower) {
        Long followerId = follower.getFollower().getId();
        Long followingId = follower.getFollowing().getId();

        if (!userRepository.existsById(followerId) || !userRepository.existsById(followingId)) {
            throw new RuntimeException("User not found");
        }

        Follower existing = followerRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (existing != null) {
            return existing;
        }

        Follower saved = followerRepository.save(follower);

    
        String message = follower.getFollower().getFirstname() + " " +
                     follower.getFollower().getLastname() +
                     " vous a suivi.";

        Notification notif = new Notification();
        notif.setMessage(message);
        notif.setUser(follower.getFollowing());

        notificationService.create(notif);

        return saved;
    }

    public Follower findByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        return followerRepository.findByFollowerIdAndFollowingId(followerId, followingId);
    }

    public void unfollowUser(Long followerId, Long followingId) {
        Follower follower = followerRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (follower != null) {
            followerRepository.delete(follower);
        }
    }
}

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

    if (followerId.equals(followingId)) {
        throw new RuntimeException("No pas vous suivre vous-même.");
    }

    var followerUser = userRepository.findById(followerId)
            .orElseThrow(() -> new RuntimeException("Follower user not found"));

    var followingUser = userRepository.findById(followingId)
            .orElseThrow(() -> new RuntimeException("Following user not found"));

    Follower existing = followerRepository.findByFollowerIdAndFollowingId(followerId, followingId);
    if (existing != null) {
        throw new RuntimeException("Vous suivez déjà cet utilisateur.");
    }

    follower.setFollower(followerUser);
    follower.setFollowing(followingUser);
    Follower saved = followerRepository.save(follower);

    String message = followerUser.getFirstname() + " " +
                     followerUser.getLastname() +
                     " vous a suivi.";

    Notification notif = new Notification();
    notif.setMessage(message);
    notif.setUser(followingUser);

    notificationService.create(notif);

        return saved;
    }

    public Follower findByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        return followerRepository.findByFollowerIdAndFollowingId(followerId, followingId);
    }
    
    public boolean isFollowing(Long followerId, Long followingId) {
    return followerRepository
            .findByFollowerIdAndFollowingId(followerId, followingId) != null;
    }

    public void unfollowUser(Long followerId, Long followingId) {
        Follower follower = followerRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (follower != null) {
            followerRepository.delete(follower);
        }
    }
}

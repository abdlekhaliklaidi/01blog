package com.dev.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.backend.entities.Follower;
import com.dev.backend.repositories.FollowerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FollowerService {

    @Autowired
    private FollowerRepository followerRepository;

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
        return followerRepository.save(follower);
    }

    public void unfollowUser(Long id) {
        followerRepository.deleteById(id);
    }
}

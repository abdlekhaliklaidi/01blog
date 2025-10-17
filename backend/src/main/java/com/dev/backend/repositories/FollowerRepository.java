package com.dev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dev.backend.entities.Follower;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findByFollowerId(Long followerId);
    List<Follower> findByFollowingId(Long followingId);
}

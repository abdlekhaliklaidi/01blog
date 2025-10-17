package com.dev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dev.backend.entities.Like;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
}

package com.dev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dev.backend.entities.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(Long authorId);
}

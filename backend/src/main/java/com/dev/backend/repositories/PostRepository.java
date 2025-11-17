package com.dev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dev.backend.entities.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.likes LEFT JOIN FETCH p.comments ORDER BY p.createdAt DESC")
    List<Post> findAllWithLikesAndComments();
    List<Post> findByAuthorId(Long authorId);

}
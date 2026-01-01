package com.dev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dev.backend.entities.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.likes LEFT JOIN FETCH p.comments ORDER BY p.createdAt DESC")
    // List<Post> findAllWithLikesAndComments();

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.likes LEFT JOIN FETCH p.comments WHERE p.hidden = false ORDER BY p.createdAt DESC")
    List<Post> findAllWithLikesAndComments();
    
    @Query("""
    SELECT p FROM Post p
    WHERE (:lastId IS NULL OR p.id > :lastId)
    ORDER BY p.id ASC
    """)
    List<Post> findPostsAfterId(
    @Param("lastId") Long lastId,
    Pageable pageable
    );

    // List<Post> findByAuthorId(Long authorId);
    List<Post> findByAuthorIdOrderByCreatedAtDesc(@Param("authorId") Long authorId);

    // List<Post> findByAuthorIdIn(List<Long> authorIds);
    List<Post> findByAuthorIdInOrderByCreatedAtDesc(@Param("authorIds") List<Long> authorIds);

    List<Post> findByAuthorId(Long authorId);

    @Query("""
    SELECT p FROM Post p
    WHERE p.author.id IN :authorIds
    AND p.hidden = false
    AND (:lastId IS NULL OR p.id < :lastId)
    ORDER BY p.id DESC
    """)
    List<Post> findFeedWithPagination(
    @Param("authorIds") List<Long> authorIds,
    @Param("lastId") Long lastId,
    Pageable pageable
);

    Optional<Post> findTopByAuthorIdOrderByCreatedAtDesc(Long authorId);
}
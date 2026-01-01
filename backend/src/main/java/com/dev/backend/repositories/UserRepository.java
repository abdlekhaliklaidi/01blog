package com.dev.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dev.backend.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // Optional<User> findByUsername(String username);
    @Query("""
    SELECT u FROM User u
    WHERE u.email <> 'admin@gmail.com'
    AND (:lastId IS NULL OR u.id > :lastId)
    ORDER BY u.id ASC
    """)
    List<User> findUsersAfterId(
    @Param("lastId") Long lastId,
    Pageable pageable
);

}
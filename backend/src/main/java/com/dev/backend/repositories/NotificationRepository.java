package com.dev.backend.repositories;

import com.dev.backend.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // List<Notification> findByUserId(Long userId);
    Page<Notification> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<Notification> findByUserIdAndIdGreaterThanOrderByIdDesc(
            Long userId,
            Long lastId
    );

    long countByUserIdAndReadFalse(Long userId);
    List<Notification> findByUserIdAndReadFalseOrderByIdDesc(Long userId);

}
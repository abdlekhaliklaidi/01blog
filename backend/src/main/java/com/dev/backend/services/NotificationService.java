package com.dev.backend.services;

import com.dev.backend.entities.Notification;
import com.dev.backend.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }

    // public List<Notification> getUserNotifications(Long userId) {
    //     return notificationRepository.findByUserId(userId);
    // }

    public Page<Notification> getNotifications(Long userId, int page, int size) {
        return notificationRepository.findByUserIdOrderByIdDesc(
                userId,
                PageRequest.of(page, size)
        );
    }

    public List<Notification> getLatestNotifications(Long userId, Long lastId) {
        return notificationRepository
                .findByUserIdAndIdGreaterThanOrderByIdDesc(userId, lastId);
    }
}

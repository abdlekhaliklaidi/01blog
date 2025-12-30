package com.dev.backend.services;

import com.dev.backend.entities.Notification;
import com.dev.backend.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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

//     @Service
//     public class NotificationService {

//     private final NotificationRepository notificationRepository;

//     private final ConcurrentHashMap<Long, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

//     @Autowired
//     public NotificationService(NotificationRepository notificationRepository) {
//         this.notificationRepository = notificationRepository;
//     }

//     public Notification create(Notification notification) {
//         Notification saved = notificationRepository.save(notification);
//         sendToUser(notification.getUser().getId(), saved);
//         return saved;
//     }

//     public Page<Notification> getNotifications(Long userId, int page, int size) {
//         return notificationRepository.findByUserIdOrderByIdDesc(userId, PageRequest.of(page, size));
//     }

//     public List<Notification> getLatestNotifications(Long userId, Long lastId) {
//         return notificationRepository.findByUserIdAndIdGreaterThanOrderByIdDesc(userId, lastId);
//     }

//     public SseEmitter subscribe(Long userId) {
//         SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
//         emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);

//         emitter.onCompletion(() -> emitters.get(userId).remove(emitter));
//         emitter.onTimeout(() -> emitters.get(userId).remove(emitter));

//         return emitter;
//     }

//     private void sendToUser(Long userId, Notification notification) {
//         if (!emitters.containsKey(userId)) return;

//         emitters.get(userId).forEach(emitter -> {
//             try {
//                 emitter.send(notification);
//             } catch (IOException e) {
//                 emitter.completeWithError(e);
//             }
//         });
//     }
// }
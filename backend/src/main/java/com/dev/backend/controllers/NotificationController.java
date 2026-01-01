package com.dev.backend.controllers;

import com.dev.backend.entities.Notification;
import com.dev.backend.services.NotificationService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // @GetMapping("/user/{userId}")
    // public List<Notification> userNotifications(@PathVariable Long userId) {
    //     return notificationService.getUserNotifications(userId);
    // }

    // @GetMapping("/user/{userId}/latest")
    // public List<Notification> latestNotifications(
    //     @PathVariable Long userId,
    //     @RequestParam Long lastId
    // ) {
    // return notificationRepository
    //         .findByUserIdAndIdGreaterThanOrderByIdDesc(userId, lastId);
    // }
    
    // @GetMapping("/user/{userId}/stream")
    // public SseEmitter streamNotifications(@PathVariable Long userId) {
    //     return notificationService.subscribe(userId);
    // }

    @GetMapping("/user/{userId}")
    public Page<Notification> getNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return notificationService.getNotifications(userId, page, size);
    }

//     @GetMapping("/user/{userId}")
//     public Map<String, Object> getNotifications(
//         @PathVariable Long userId,
//         @RequestParam(defaultValue = "0") int page,
//         @RequestParam(defaultValue = "6") int size
//     ) {
//     Page<Notification> notificationsPage = notificationService.getNotifications(userId, page, size);

//     Map<String, Object> response = new HashMap<>();
//     response.put("content", notificationsPage.getContent());
//     response.put("page", notificationsPage.getNumber());
//     response.put("size", notificationsPage.getSize());
//     response.put("totalElements", notificationsPage.getTotalElements());
//     response.put("totalPages", notificationsPage.getTotalPages());

//     return response;
// }

    @GetMapping("/user/{userId}/latest")
    public List<Notification> getLatest(
            @PathVariable Long userId,
            @RequestParam Long lastId
    ) {
        return notificationService.getLatestNotifications(userId, lastId);
    }

    @PostMapping
    public Notification send(@RequestBody Notification notification) {
        return notificationService.create(notification);
    }

    @GetMapping("/user/{userId}/unread/count")
    public long unreadCount(@PathVariable Long userId) {
    return notificationService.countUnread(userId);
    }

   @PutMapping("/{id}/read")
   public Map<String, Long> markRead(@PathVariable Long id) {
    notificationService.markAsRead(id);
    long unreadCount = notificationService.countUnread(
        notificationService.getNotificationById(id).getUser().getId()
    );
    return Map.of("unreadCount", unreadCount);
    }

    @PutMapping("/{id}/unread")
    public Map<String, Long> markUnread(@PathVariable Long id) {
    notificationService.markAsUnread(id);
    long unreadCount = notificationService.countUnread(
        notificationService.getNotificationById(id).getUser().getId()
    );
    return Map.of("unreadCount", unreadCount);
    }

    @PutMapping("/user/{userId}/read-all")
    public Map<String, Long> readAll(@PathVariable Long userId) {
    notificationService.markAllAsRead(userId);
    long count = notificationService.countUnread(userId);
    Map<String, Long> response = new HashMap<>();
    response.put("unreadCount", count);
    return response;
    }

}

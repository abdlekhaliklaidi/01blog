package com.dev.backend.controllers;

import com.dev.backend.entities.Notification;
import com.dev.backend.services.NotificationService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

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
    
    @GetMapping("/user/{userId}")
    public Page<Notification> getNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return notificationService.getNotifications(userId, page, size);
    }

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
}

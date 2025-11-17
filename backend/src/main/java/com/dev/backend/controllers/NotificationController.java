package com.dev.backend.controllers;

import com.dev.backend.entities.Notification;
import com.dev.backend.services.NotificationService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public List<Notification> userNotifications(@PathVariable Long userId) {
        return notificationService.getUserNotifications(userId);
    }

    @PostMapping
    public Notification send(@RequestBody Notification notification) {
        return notificationService.create(notification);
    }
}

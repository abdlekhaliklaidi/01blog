package com.dev.backend.dto;

import com.dev.backend.entities.Like;

public class LikeDTO {

    private Long id;
    private Long userId;
    private String userEmail;

    public LikeDTO(Like like) {
        this.id = like.getId();
        this.userId = like.getUser().getId();
        this.userEmail = like.getUser().getEmail();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }
}
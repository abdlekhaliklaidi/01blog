package com.dev.backend.dto;

import com.dev.backend.entities.Comment;
import java.time.LocalDateTime;

public class CommentDTO {

    private Long id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();

        if (comment.getUser() != null) {
            this.authorName = comment.getUser().getFirstname() + " " + comment.getUser().getLastname();
        } else {
            this.authorName = "Unknown";
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
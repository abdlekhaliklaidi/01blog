package com.dev.backend.dto;

import com.dev.backend.entities.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;

public class PostDTO {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private String authorFirstName;

    private int likesCount;
    private boolean likedByCurrentUser;

    private List<CommentDTO> comments;
    private List<LikeDTO> likes;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();

        if (post.getImageBase64() != null && !post.getImageBase64().isEmpty()) {
            this.imageUrl = "data:image/jpeg;base64," + post.getImageBase64();
        } else {
            this.imageUrl = null;
        }

        this.createdAt = post.getCreatedAt();
        this.authorFirstName = post.getAuthor() != null 
                ? post.getAuthor().getFirstname() 
                : "Unknown";

        this.likes = post.getLikes() != null
        ? post.getLikes().stream()
            .map(like -> new LikeDTO(like))
            .collect(Collectors.toList())
        : List.of();
        
        this.likesCount = this.likes.size();

        String currentEmail;
        try {
            currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception ex) {
            currentEmail = null;
        }

        if (currentEmail != null && post.getLikes() != null) {
            final String email = currentEmail;
            this.likedByCurrentUser = post.getLikes().stream()
                    .anyMatch(like -> like.getUser().getEmail().equals(email));
        } else {
            this.likedByCurrentUser = false;
        }

        this.comments = post.getComments() != null
                ? post.getComments().stream().map(CommentDTO::new).collect(Collectors.toList())
                : List.of();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getAuthorFirstName() { return authorFirstName; }
    public void setAuthorFirstName(String authorFirstName) { this.authorFirstName = authorFirstName; }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public boolean isLikedByCurrentUser() { return likedByCurrentUser; }
    public void setLikedByCurrentUser(boolean likedByCurrentUser) { this.likedByCurrentUser = likedByCurrentUser; }

    public List<CommentDTO> getComments() { return comments; }
    public void setComments(List<CommentDTO> comments) { this.comments = comments; }

    public List<LikeDTO> getLikes() { return likes; }
    public void setLikes(List<LikeDTO> likes) { this.likes = likes; }
}

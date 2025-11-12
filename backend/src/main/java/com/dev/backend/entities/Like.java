package com.dev.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "post_like", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-like")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference(value = "post-like")
    private Post post;

    private LocalDateTime likedAt;

    @PrePersist
    protected void onLike() {
        likedAt = LocalDateTime.now();
    }

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public User getUser() { 
        return user; 
    }
    
    public void setUser(User user) { 
        this.user = user; 
    }

    public Post getPost() { 
        return post; 
    }

    public void setPost(Post post) { 
        this.post = post; 
    }

    public LocalDateTime getLikedAt() { 
        return likedAt; 
    }

    public void setLikedAt(LocalDateTime likedAt) { 
        this.likedAt = likedAt; 
    }
}
package com.dev.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "user_follow", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"follower_id", "following_id"})
})
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "follower_id")
    @JsonBackReference(value = "following-follower")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
    @JsonBackReference(value = "follower-following")
    private User following;

    private LocalDateTime followedAt;

    @PrePersist
    protected void onFollow() {
        this.followedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(LocalDateTime followedAt) {
        this.followedAt = followedAt;
    }
}

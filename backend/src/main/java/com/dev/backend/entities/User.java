package com.dev.backend.entities;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String lastname;
    private String firstname;
    private String genre;
    
    @NotBlank
    @Pattern(
     regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$",
     message = "Email doit être un compte Gmail"
    )
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String avatar;

    private String bio;
    
    @Column(nullable = false)
    private boolean banned = false;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-post")
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-comment")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-like")
    private List<Like> likes;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "follower-following")
    private List<Follower> followers;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "following-follower")
    private List<Follower> followings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Notification> notifications;


    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reportsMade;

    @OneToMany(mappedBy = "reportedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reportsReceived;

    private String role = "USER";
    
    public String getRole() {
    return role;
    }
    
    public boolean isBanned() {
    return banned;
    }

    public void setBanned(boolean banned) {
    this.banned = banned;
    }

    public void setRole(String role) {
    this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    public List<Like> getLikes() { return likes; }
    public void setLikes(List<Like> likes) { this.likes = likes; }
    public List<Follower> getFollowers() { return followers; }
    public void setFollowers(List<Follower> followers) { this.followers = followers; }
    public List<Follower> getFollowings() { return followings; }
    public void setFollowings(List<Follower> followings) { this.followings = followings; }
    public List<Report> getReportsMade() { return reportsMade; }
    public void setReportsMade(List<Report> reportsMade) { this.reportsMade = reportsMade; }
    public List<Report> getReportsReceived() { return reportsReceived; }
    public void setReportsReceived(List<Report> reportsReceived) { this.reportsReceived = reportsReceived; }
    public String getAvatar() { return avatar; } 
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public List<Notification> getNotifications() { return notifications; }
    public void setNotifications(List<Notification> notifications) { this.notifications = notifications; }
}

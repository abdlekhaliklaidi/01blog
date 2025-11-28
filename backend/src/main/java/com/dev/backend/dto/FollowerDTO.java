package com.dev.backend.dto;

import com.dev.backend.entities.Follower;

public class FollowerDTO {
    private Long id;
    private String name;
    private String avatar;
    private String status;
    private boolean following;

    public FollowerDTO(Follower f, boolean isFollower) {
        if (isFollower) {
            this.id = f.getFollower().getId();
            this.name = f.getFollower().getFirstname() + " " + f.getFollower().getLastname();
            this.avatar = f.getFollower().getAvatar();
            this.status = "accepted"; 
            this.following = false;
        } else {
            this.id = f.getFollowing().getId();
            this.name = f.getFollowing().getFirstname() + " " + f.getFollowing().getLastname();
            this.avatar = f.getFollowing().getAvatar();
            this.status = "accepted";
            this.following = true;
        }
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAvatar() { return avatar; }
    public String getStatus() { return status; }
    public boolean isFollowing() { return following; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setStatus(String status) { this.status = status; }
    public void setFollowing(boolean following) { this.following = following; }
}

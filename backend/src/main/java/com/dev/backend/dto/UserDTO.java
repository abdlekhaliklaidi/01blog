package com.dev.backend.dto;

public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String avatar;
    private String bio;

    public UserDTO(Long id, String firstname, String lastname, String email, String avatar, String bio) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.avatar = avatar;
        this.bio = bio;
    }

    public Long getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public String getAvatar() { return avatar; }
    public String getBio() { return bio; }


    public void setId(Long id) { this.id = id; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public void setEmail(String email) { this.email = email; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setBio(String bio) { this.bio = bio; }
}

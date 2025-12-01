package com.dev.backend.dto;

public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String avatar;

    public UserDTO(Long id, String firstname, String lastname, String email, String avatar) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.avatar = avatar;
    }

    public Long getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public String getAvatar() { return avatar; }

    public void setId(Long id) { this.id = id; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public void setEmail(String email) { this.email = email; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}

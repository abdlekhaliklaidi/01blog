package com.dev.backend.dto;

public class AdminUserDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private boolean banned;

    public AdminUserDTO(
            Long id,
            String firstname,
            String lastname,
            String email,
            boolean banned
    ) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.banned = banned;
    }

    public Long getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public boolean isBanned() { return banned; }
}

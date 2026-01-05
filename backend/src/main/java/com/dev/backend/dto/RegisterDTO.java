package com.dev.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public class RegisterDTO {

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
    private String genre;

    @NotBlank
    @Pattern(
      regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$",
      message = "Email doit être un compte Gmail"
    )
    private String email;

    @NotBlank
    @Pattern(
      regexp = "^[A-Z].{7,}$",
      message = "Mot de passe invalide"
    )
    private String password;

    // getters & setters
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

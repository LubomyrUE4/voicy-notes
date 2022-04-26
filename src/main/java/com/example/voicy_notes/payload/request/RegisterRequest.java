package com.example.voicy_notes.payload.request;

import javax.validation.constraints.*;

public class RegisterRequest {
    @NotBlank
    @Size(min=3, message = "Should have at least 3 characters!")
    @Size(max=20, message = "Should have not more than 20 characters!")
    private String username;

    @NotBlank
    @Size(min=3, message = "Should have at least 3 characters!")
    @Size(max=20, message = "Should have not more than 20 characters!")
    @Email
    private String email;

    @NotBlank
    @Size(min=3, message = "Should have at least 3 characters!")
    @Size(max=20, message = "Should have not more than 20 characters!")
    private String password;

    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
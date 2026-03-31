package com.example.Energy_Dashboard.service.dto;

public class SignupResponse {
    private final Long userId;
    private final String username;
    private final String emailId;
    private final String role;
    private final String message;

    public SignupResponse(Long userId, String username, String emailId, String role, String message) {
        this.userId = userId;
        this.username = username;
        this.emailId = emailId;
        this.role = role;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }
}

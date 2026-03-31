package com.example.Energy_Dashboard.service.dto;

public class AuthResponse {
    private final String accessToken;
    private final String tokenType;
    private final long expiresInMs;
    private final String username;
    private final String role;

    public AuthResponse(String accessToken, String tokenType, long expiresInMs, String username, String role) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresInMs = expiresInMs;
        this.username = username;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresInMs() {
        return expiresInMs;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}

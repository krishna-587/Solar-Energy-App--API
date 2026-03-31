package com.example.Energy_Dashboard.service.dto;

import com.example.Energy_Dashboard.model.Role;

public class UserSummary {
    private final Long id;
    private final String username;
    private final String emailId;
    private final Role role;

    public UserSummary(Long id, String username, String emailId, Role role) {
        this.id = id;
        this.username = username;
        this.emailId = emailId;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailId() {
        return emailId;
    }

    public Role getRole() {
        return role;
    }
}

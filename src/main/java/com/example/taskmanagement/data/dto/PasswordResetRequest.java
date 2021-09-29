package com.example.taskmanagement.data.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String username;
    private String password;
}

package com.example.taskmanagement.data.dto;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}

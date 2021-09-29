package com.example.taskmanagement.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationDetail {
    private String jwtToken;
    private String username;
}

package com.example.taskmanagement.data.dto;

import com.example.taskmanagement.data.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;
}

package com.example.taskmanagement.data.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class IndividualRegistrationRequest {
    @Size(min = 2, message = "username cannot be less than 2 characters")
    private String username;
    @Email(message = "email be valid")
    private String email;
    @Size(min = 4, max = 15, message = "Password must be valid")
    private String password;
}

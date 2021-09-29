package com.example.taskmanagement.data.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class OrganizationRegistrationRequest {
    @NotBlank(message = "company name cannot be blank")
    private String companyName;
    @Email(message = "email be valid")
    private String email;
    @Size(min = 4, max = 15, message = "Password must be valid")
    private String password;
}

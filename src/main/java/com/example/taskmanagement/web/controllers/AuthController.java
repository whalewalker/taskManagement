package com.example.taskmanagement.web.controllers;


import com.example.taskmanagement.data.dto.*;
import com.example.taskmanagement.data.model.Token;
import com.example.taskmanagement.services.AuthService;
import com.example.taskmanagement.web.exceptions.TokenException;
import com.example.taskmanagement.web.exceptions.UserException;
import com.example.taskmanagement.web.payloads.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("api/v1/user/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register/individual")
    public ResponseEntity<?> registerIndividual(@RequestBody @Valid IndividualRegistrationRequest individualRegistrationRequest) {
        try {
            UserDto userDTO = authService.registerIndividual(individualRegistrationRequest);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (UserException userException) {
            return new ResponseEntity<>(new ApiResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register/organization")
    public ResponseEntity<?> registerOrganization(@RequestBody @Valid OrganizationRegistrationRequest organizationRegistrationRequest) {
        try {
            UserDto userDTO = authService.registerOrganization(organizationRegistrationRequest);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (UserException userException) {
            return new ResponseEntity<>(new ApiResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid IndividualRegistrationRequest request) {
        try {
            UserDto userDTO = authService.registerAdmin(request);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (UserException userException) {
            return new ResponseEntity<>(new ApiResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) throws AuthenticationException {
        AuthenticationDetail authenticationDetail = authService.login(loginRequest);
        return new ResponseEntity<>(authenticationDetail, HttpStatus.OK);
    }

    @GetMapping("/password/reset/{username}")
    public ResponseEntity<?> forgetPassword(@PathVariable String username) {
        try {
            Token passwordResetToken = authService.generatePasswordResetToken(username);
            return new ResponseEntity<>(passwordResetToken, HttpStatus.OK);
        } catch (UserException userException) {
            return new ResponseEntity<>(new ApiResponse(false, userException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/password/reset/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @Valid @RequestBody PasswordResetRequest request) {
        try {
            authService.resetUserPassword(request, token);
            return new ResponseEntity<>(new ApiResponse(true, "Password reset successful"), HttpStatus.OK);
        } catch (TokenException | UserException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

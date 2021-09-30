package com.example.taskmanagement.services;


import com.example.taskmanagement.data.dto.*;
import com.example.taskmanagement.data.model.Token;
import com.example.taskmanagement.web.exceptions.TokenException;
import com.example.taskmanagement.web.exceptions.UserException;

public interface AuthService {
    UserDto registerIndividual(IndividualRegistrationRequest details) throws UserException;

    UserDto registerOrganization(OrganizationRegistrationRequest details) throws UserException;

    UserDto registerAdmin(IndividualRegistrationRequest details) throws UserException;

    AuthenticationDetail login(LoginRequest loginRequest);

    UserDto findUserByUserName(String username);

    void resetUserPassword(PasswordResetRequest request, String passwordResetToken) throws UserException, TokenException;

    void updateUserPassword(PasswordChangeRequest request) throws UserException;

    Token generatePasswordResetToken(String username) throws UserException;

    UserDto updateUser(String userId, UserDto userDto) throws UserException;
}

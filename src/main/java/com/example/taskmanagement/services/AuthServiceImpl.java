package com.example.taskmanagement.services;

import com.example.taskmanagement.data.dto.*;
import com.example.taskmanagement.data.model.Role;
import com.example.taskmanagement.data.model.Token;
import com.example.taskmanagement.data.model.TokenType;
import com.example.taskmanagement.data.model.User;
import com.example.taskmanagement.data.repository.TokenRepository;
import com.example.taskmanagement.data.repository.UserRepository;
import com.example.taskmanagement.services.security.CustomUserDetails;
import com.example.taskmanagement.services.security.JwtTokenProvider;
import com.example.taskmanagement.web.exceptions.TokenException;
import com.example.taskmanagement.web.exceptions.UserException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.taskmanagement.data.model.Role.*;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetails customUserDetails;

    @Override
    public UserDto registerIndividual(IndividualRegistrationRequest details) throws UserException {
        return createUser(details, INDIVIDUAL);
    }

    private boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDto registerOrganization(OrganizationRegistrationRequest details) throws UserException {
        if (emailExists(details.getEmail())) {
            throw new UserException(String.format("company already exits with %s", details.getEmail()));
        }
        User newOrganization = mapper.map(details, User.class);
        newOrganization.setUsername(details.getCompanyName());
        newOrganization.getRoles().add(ORGANIZATION);
        newOrganization.setPassword(passwordEncoder.encode(details.getPassword()));

        User savedOrganization = userRepository.save(newOrganization);
        return mapper.map(savedOrganization, UserDto.class);
    }

    private boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDto registerAdmin(IndividualRegistrationRequest details) throws UserException {
        return createUser(details, ADMIN);
    }

    private UserDto createUser(IndividualRegistrationRequest details, Role admin) throws UserException {
        if (usernameExists(details.getUsername())) {
            throw new UserException(String.format("username already exits with %s", details.getUsername()));
        }
        User newAdmin = mapper.map(details, User.class);
        newAdmin.getRoles().add(admin);
        newAdmin.setPassword(passwordEncoder.encode(details.getPassword()));

        User savedUser = userRepository.save(newAdmin);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public AuthenticationDetail login(LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = customUserDetails.loadUserByUsername(loginRequest.getUsername());
        final String token = jwtTokenProvider.generateToken(userDetails);
        User user = internalFindUserByUsername(loginRequest.getUsername());
        return new AuthenticationDetail(token, user.getUsername());
    }

    private User internalFindUserByUsername(String name) {
        return userRepository.findByUsername(name).orElse(null);
    }

    @Override
    public UserDto findUserByUserName(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with user name " + username)));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public void resetUserPassword(PasswordResetRequest request, String passwordResetToken) throws UserException, TokenException {
        String username = request.getUsername();
        String newPassword = request.getPassword();
        User userToResetPassword = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("No user found with user name " + username));
        Token token = tokenRepository.findByToken(passwordResetToken)
                .orElseThrow(() -> new TokenException(String.format("No token with value %s found", passwordResetToken)));
        if (token.getExpiry().isBefore(LocalDateTime.now())) {
            throw new TokenException("This password reset token has expired ");
        }
        if (!token.getUser().getId().equals(userToResetPassword.getId())) {
            throw new TokenException("This password rest token does not belong to this user");
        }
        userToResetPassword.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToResetPassword);
        tokenRepository.delete(token);
    }

    @Override
    public void updateUserPassword(PasswordChangeRequest request) throws UserException {
        String username = request.getUsername();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();
        User userToChangePassword = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("No user found with user name " + username));

        boolean passwordMatch = passwordEncoder.matches(oldPassword, userToChangePassword.getPassword());
        if (!passwordMatch) {
            throw new UserException("Passwords do not match");
        }
        userToChangePassword.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToChangePassword);
    }

    @Override
    public Token generatePasswordResetToken(String username) throws UserException {
        User userToResetPassword = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("No user found with user name " + username));
        Token token = new Token();
        token.setType(TokenType.PASSWORD_RESET);
        token.setUser(userToResetPassword);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiry(LocalDateTime.now().plusMinutes(30));
        return tokenRepository.save(token);
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) throws UserException {
            User userToUpdate = userRepository.findById(userId).orElseThrow(
                    () -> new UserException(String.format("User with this id %s does not exist", userDto)));

            mapper.map(userDto, userToUpdate);
            User savedUser = userRepository.save(userToUpdate);
            return mapper.map(savedUser, UserDto.class);
        }
}

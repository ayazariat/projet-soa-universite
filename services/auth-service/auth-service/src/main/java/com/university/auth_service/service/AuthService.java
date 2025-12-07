package com.university.auth_service.service;

import com.university.auth_service.dto.AuthResponse;
import com.university.auth_service.dto.LoginRequest;
import com.university.auth_service.dto.RegisterRequest;
import com.university.auth_service.dto.UpdatePasswordRequest;
import com.university.auth_service.dto.UserDTO;
import org.springframework.security.core.Authentication;

public interface AuthService {
    AuthResponse authenticate(LoginRequest request);

    UserDTO register(RegisterRequest request);

    void logout(Authentication authentication);

    void updatePassword(String username, UpdatePasswordRequest request);
}

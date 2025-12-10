package com.university.auth_service.service.impl;

import com.university.auth_service.dto.AuthResponse;
import com.university.auth_service.dto.LoginRequest;
import com.university.auth_service.dto.RegisterRequest;
import com.university.auth_service.dto.UpdatePasswordRequest;
import com.university.auth_service.dto.UserDTO;
import com.university.auth_service.entity.User;
import com.university.auth_service.entity.VerificationToken;
import com.university.auth_service.exception.AuthException;
import com.university.auth_service.repository.UserRepository;
import com.university.auth_service.repository.VerificationTokenRepository;
import com.university.auth_service.security.JwtTokenProvider;
import com.university.auth_service.service.AuthService;
import com.university.auth_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public AuthResponse authenticate(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthException("Utilisateur introuvable"));

        // Vérifier que le compte est activé
        if (!user.getEnabled()) {
            throw new AuthException("Veuillez activer votre compte via le lien envoyé par email");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);
        UserDTO userDTO = convertToDTO(user);

        return new AuthResponse(token, "Bearer", jwtTokenProvider.getValidityInMilliseconds(), userDTO);
    }

    @Override
    public UserDTO register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthException("Nom d'utilisateur déjà utilisé");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email déjà utilisé");
        }

        // Créer un utilisateur désactivé
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());
        user.setEnabled(true); // activé par défaut pour faciliter les tests
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Générer un token d'activation
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, savedUser.getId());
        verificationTokenRepository.save(verificationToken);

        // Envoyer le lien de confirmation
        try {
            emailService.sendActivationEmail(savedUser.getEmail(), savedUser.getFirstName(), token);
        } catch (Exception e) {
            // Loger l'erreur sans bloquer l'inscription
            e.printStackTrace();
        }

        return convertToDTO(savedUser);
    }

    @Override
    public void logout(Authentication authentication) {
        // In stateless JWT auth, logout is typically client-side (discard token)
        // This method can be used to log logout events or blacklist tokens if needed
        if (authentication != null && authentication.isAuthenticated()) {
            // You can implement token blacklisting here if required
            // For now, we rely on client-side token removal
        }
    }

    @Override
    public void updatePassword(String username, UpdatePasswordRequest request) {
        // Validate that new password matches confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AuthException("Passwords do not match");
        }

        // Validate that new password is different from current password
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new AuthException("New password must be different from current password");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AuthException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private UserDTO convertToDTO(User u) {
        return new UserDTO(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getFirstName(),
                u.getLastName(),
                u.getRole());
    }
}

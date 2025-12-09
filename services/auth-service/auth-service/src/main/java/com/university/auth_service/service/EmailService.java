package com.university.auth_service.service;

public interface EmailService {
    void sendActivationEmail(String to, String firstName, String token);
}


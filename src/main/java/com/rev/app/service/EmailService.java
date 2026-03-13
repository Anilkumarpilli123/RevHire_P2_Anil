package com.rev.app.service;

public interface EmailService {
    void sendPasswordResetEmail(String to, String token);
}

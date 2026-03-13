package com.rev.app.service.impl;

import com.rev.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = "http://localhost:8081/reset-password?token=" + token;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Password Reset Request");
            message.setText("To reset your password, click the link below:\n" + resetUrl);
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: {}", to);
        } catch (org.springframework.mail.MailException e) {
            logger.error("Failed to send password reset email to {}. Error: {}", to, e.getMessage());
            // Log the link as a fallback for local testing
            logger.warn("FALLBACK: If this is local development, use this link to reset password: {}", resetUrl);
            logger.warn(
                    "SUPPRESSED ERROR: We encountered an issue sending the email, but continuing for local testing.");
        }
    }
}

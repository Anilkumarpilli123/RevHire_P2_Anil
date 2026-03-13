package com.rev.app.service.impl;

import com.rev.app.config.JwtUtils;
import com.rev.app.dto.JwtResponse;
import com.rev.app.dto.LoginRequest;
import com.rev.app.dto.SignupRequest;
import com.rev.app.entity.EmployerProfile;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.PasswordResetToken;
import com.rev.app.entity.User;
import com.rev.app.repository.EmployerRepository;
import com.rev.app.repository.JobSeekerRepository;
import com.rev.app.repository.PasswordResetTokenRepository;
import com.rev.app.repository.UserRepository;
import com.rev.app.service.AuthService;
import com.rev.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        return new JwtResponse(jwt, user.getEmail(), user.getRole());
    }

    @Override
    @Transactional
    public User registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(encoder.encode(signupRequest.getPassword()))
                .role(signupRequest.getRole().toLowerCase())
                .build();

        User savedUser = userRepository.save(user);

        // Create profile based on role
        if ("seeker".equalsIgnoreCase(signupRequest.getRole())) {
            JobSeekerProfile seeker = JobSeekerProfile.builder()
                    .user(savedUser)
                    .name(signupRequest.getName())
                    .build();
            jobSeekerRepository.save(seeker);
        } else if ("employer".equalsIgnoreCase(signupRequest.getRole())) {
            EmployerProfile employer = EmployerProfile.builder()
                    .user(savedUser)
                    .build();
            employerRepository.save(employer);
        }

        return savedUser;
    }

    @Override
    @Transactional
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: Email does not exist."));

        String token = UUID.randomUUID().toString();

        // Find existing token or create a new one to prevent unique constraint
        // violation
        PasswordResetToken resetToken = tokenRepository.findByUser(user).orElse(null);
        if (resetToken != null) {
            resetToken.setToken(token);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        } else {
            resetToken = PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusHours(24))
                    .build();
        }

        tokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isExpired())
                .orElse(false);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .filter(t -> !t.isExpired())
                .orElseThrow(() -> new RuntimeException("Error: Invalid or expired reset token."));

        User user = resetToken.getUser();
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        // Delete the token after successful reset
        tokenRepository.delete(resetToken);
    }
}

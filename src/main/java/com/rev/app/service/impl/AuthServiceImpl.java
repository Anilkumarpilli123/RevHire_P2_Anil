package com.rev.app.service.impl;

import com.rev.app.config.JwtUtils;
import com.rev.app.dto.JwtResponse;
import com.rev.app.dto.LoginRequest;
import com.rev.app.dto.SignupRequest;
import com.rev.app.entity.EmployerProfile;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.User;
import com.rev.app.repository.EmployerRepository;
import com.rev.app.repository.JobSeekerRepository;
import com.rev.app.repository.UserRepository;
import com.rev.app.service.AuthService;
import com.rev.app.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

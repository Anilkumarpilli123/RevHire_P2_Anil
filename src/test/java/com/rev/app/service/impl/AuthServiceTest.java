package com.rev.app.service.impl;

import com.rev.app.dto.SignupRequest;
import com.rev.app.entity.User;
import com.rev.app.repository.EmployerRepository;
import com.rev.app.repository.JobSeekerRepository;
import com.rev.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobSeekerRepository jobSeekerRepository;

    @Mock
    private EmployerRepository employerRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private SignupRequest signupRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest("test@example.com", "password", "Test User", "seeker");
        testUser = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role("seeker")
                .build();
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User registeredUser = authService.registerUser(signupRequest);

        assertNotNull(registeredUser);
        assertEquals("test@example.com", registeredUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jobSeekerRepository, times(1)).save(any());
    }

    @Test
    void registerUser_AlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.registerUser(signupRequest));
    }
}

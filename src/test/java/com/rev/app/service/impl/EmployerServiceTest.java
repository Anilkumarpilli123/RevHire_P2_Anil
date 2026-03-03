package com.rev.app.service.impl;

import com.rev.app.entity.EmployerProfile;
import com.rev.app.entity.User;
import com.rev.app.repository.EmployerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployerServiceTest {

    @Mock
    private EmployerRepository employerRepository;

    @InjectMocks
    private EmployerServiceImpl employerService;

    private EmployerProfile testProfile;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1).email("e@e.com").build();
        testProfile = EmployerProfile.builder().id(1).user(testUser).name("E").build();
    }

    @Test
    void getProfileByUser_Success() {
        when(employerRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));

        EmployerProfile found = employerService.getProfileByUser(testUser);

        assertNotNull(found);
        assertEquals("E", found.getName());
    }

    @Test
    void updateProfile_Success() {
        when(employerRepository.save(any(EmployerProfile.class))).thenReturn(testProfile);

        EmployerProfile saved = employerService.updateProfile(testProfile);

        assertNotNull(saved);
        verify(employerRepository, times(1)).save(testProfile);
    }
}

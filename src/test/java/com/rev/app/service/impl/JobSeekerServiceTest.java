package com.rev.app.service.impl;

import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.User;
import com.rev.app.repository.JobSeekerRepository;
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
class JobSeekerServiceTest {

    @Mock
    private JobSeekerRepository jobSeekerRepository;

    @InjectMocks
    private JobSeekerServiceImpl jobSeekerService;

    private JobSeekerProfile testProfile;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1).email("s@s.com").build();
        testProfile = JobSeekerProfile.builder().id(1).user(testUser).name("S").build();
    }

    @Test
    void getProfileByUser_Success() {
        when(jobSeekerRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));

        JobSeekerProfile found = jobSeekerService.getProfileByUser(testUser);

        assertNotNull(found);
        assertEquals("S", found.getName());
    }

    @Test
    void updateProfile_Success() {
        when(jobSeekerRepository.save(any(JobSeekerProfile.class))).thenReturn(testProfile);

        JobSeekerProfile saved = jobSeekerService.updateProfile(testProfile);

        assertNotNull(saved);
        verify(jobSeekerRepository, times(1)).save(testProfile);
    }
}

package com.rev.app.service.impl;

import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.Resume;
import com.rev.app.repository.ResumeRepository;
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
class ResumeServiceTest {

    @Mock
    private ResumeRepository resumeRepository;

    @InjectMocks
    private ResumeServiceImpl resumeService;

    private JobSeekerProfile testSeeker;
    private Resume testResume;

    @BeforeEach
    void setUp() {
        testSeeker = JobSeekerProfile.builder().id(1).name("S").build();
        testResume = Resume.builder().id(1).jobSeeker(testSeeker).resumePath("/path/r.pdf").build();
    }

    @Test
    void getResumeBySeeker_Success() {
        when(resumeRepository.findByJobSeeker(testSeeker)).thenReturn(Optional.of(testResume));

        Resume found = resumeService.getResumeBySeeker(testSeeker);

        assertNotNull(found);
        assertEquals("/path/r.pdf", found.getResumePath());
    }

    @Test
    void saveResume_Success() {
        when(resumeRepository.save(any(Resume.class))).thenReturn(testResume);

        Resume saved = resumeService.saveResume(testResume);

        assertNotNull(saved);
        verify(resumeRepository, times(1)).save(testResume);
    }
}

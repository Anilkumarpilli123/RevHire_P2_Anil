package com.rev.app.service.impl;

import com.rev.app.entity.Application;
import com.rev.app.entity.Company;
import com.rev.app.entity.Job;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.repository.ApplicationRepository;
import com.rev.app.repository.EmployerRepository;
import com.rev.app.repository.ResumeRepository;
import com.rev.app.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private EmployerRepository employerRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private Job testJob;
    private JobSeekerProfile testSeeker;
    private Application testApplication;

    @BeforeEach
    void setUp() {
        testJob = Job.builder().id(1).title("Java Dev").company(new Company()).build();
        testSeeker = JobSeekerProfile.builder().id(1).name("John Doe").build();
        testApplication = Application.builder()
                .id(1)
                .job(testJob)
                .jobSeeker(testSeeker)
                .status("APPLIED")
                .build();
    }

    @Test
    void applyForJob_Success() {
        when(applicationRepository.existsByJobAndJobSeeker(testJob, testSeeker)).thenReturn(false);
        when(resumeRepository.findByJobSeeker(testSeeker)).thenReturn(Optional.empty());
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        Application result = applicationService.applyForJob(testJob, testSeeker, "I love Java");

        assertNotNull(result);
        assertEquals("APPLIED", result.getStatus());
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    void applyForJob_AlreadyApplied() {
        when(applicationRepository.existsByJobAndJobSeeker(testJob, testSeeker)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> applicationService.applyForJob(testJob, testSeeker, "test"));
    }

    @Test
    void updateStatus_Success() {
        when(applicationRepository.findById(1)).thenReturn(Optional.of(testApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        applicationService.updateStatus(1, "ACCEPTED", "Great candidate");

        assertEquals("ACCEPTED", testApplication.getStatus());
        verify(applicationRepository, times(1)).save(testApplication);
    }

    @Test
    void withdrawApplication_Success() {
        when(applicationRepository.findById(1)).thenReturn(Optional.of(testApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        applicationService.withdrawApplication(1, "Better offer");

        assertEquals("WITHDRAWN", testApplication.getStatus());
        verify(applicationRepository, times(1)).save(testApplication);
    }

    @Test
    void bulkUpdateStatus_Success() {
        when(applicationRepository.findById(anyInt())).thenReturn(Optional.of(testApplication));

        applicationService.bulkUpdateStatus(List.of(1, 2), "REJECTED", "Sorry");

        verify(applicationRepository, times(2)).save(any(Application.class));
    }
}

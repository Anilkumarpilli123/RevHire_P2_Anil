package com.rev.app.service.impl;

import com.rev.app.entity.Company;
import com.rev.app.entity.Job;
import com.rev.app.repository.ApplicationRepository;
import com.rev.app.repository.FavoriteJobRepository;
import com.rev.app.repository.JobRepository;
import com.rev.app.repository.JobSeekerRepository;
import com.rev.app.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private FavoriteJobRepository favoriteJobRepository;

    @Mock
    private JobSeekerRepository jobSeekerRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    private Job testJob;
    private Company testCompany;

    @BeforeEach
    void setUp() {
        testCompany = Company.builder().id(1).name("Test Corp").build();
        testJob = Job.builder()
                .id(1)
                .title("Software Engineer")
                .company(testCompany)
                .status("OPEN")
                .build();
    }

    @Test
    void getJobById_Success() {
        when(jobRepository.findById(1)).thenReturn(Optional.of(testJob));

        Job foundJob = jobService.getJobById(1);

        assertNotNull(foundJob);
        assertEquals("Software Engineer", foundJob.getTitle());
        verify(jobRepository, times(1)).findById(1);
    }

    @Test
    void getJobById_NotFound() {
        when(jobRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> jobService.getJobById(1));
    }

    @Test
    void createJob_Success() {
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);
        when(jobSeekerRepository.findAll()).thenReturn(new ArrayList<>());

        Job savedJob = jobService.createJob(testJob);

        assertNotNull(savedJob);
        verify(jobRepository, times(1)).save(testJob);
    }

    @Test
    void updateJobStatus_Success() {
        when(jobRepository.findById(1)).thenReturn(Optional.of(testJob));
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        jobService.updateJobStatus(1, "CLOSED");

        assertEquals("CLOSED", testJob.getStatus());
        verify(jobRepository, times(1)).save(testJob);
    }

    @Test
    void deleteJob_Success() {
        when(jobRepository.findById(1)).thenReturn(Optional.of(testJob));
        when(applicationRepository.findByJob(testJob)).thenReturn(new ArrayList<>());

        jobService.deleteJob(1);

        verify(applicationRepository, times(1)).deleteAll(anyList());
        verify(jobRepository, times(1)).deleteById(1);
    }

    @Test
    void getJobsByCompany_Success() {
        when(jobRepository.findByCompany_Id(1)).thenReturn(Arrays.asList(testJob));

        List<Job> jobs = jobService.getJobsByCompany(1);

        assertFalse(jobs.isEmpty());
        assertEquals(1, jobs.size());
        verify(jobRepository, times(1)).findByCompany_Id(1);
    }
}

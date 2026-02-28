package com.rev.app.service.impl;

import com.rev.app.entity.Application;
import com.rev.app.entity.Job;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.Resume;
import com.rev.app.repository.ApplicationRepository;
import com.rev.app.repository.EmployerRepository;
import com.rev.app.repository.ResumeRepository;
import com.rev.app.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private com.rev.app.service.NotificationService notificationService;

    @Override
    public Application applyForJob(Job job, JobSeekerProfile jobSeeker, String coverLetter) {
        System.out.println("DEBUG: applyForJob started for job=" + job.getId() + ", seeker=" + jobSeeker.getId());
        if (applicationRepository.existsByJobAndJobSeeker(job, jobSeeker)) {
            System.out.println("DEBUG: Already applied for this job");
            throw new RuntimeException("You have already applied for this job");
        }

        Resume resume = resumeRepository.findByJobSeeker(jobSeeker).orElse(null);
        System.out.println("DEBUG: Resume found: " + (resume != null));

        Application application = Application.builder()
                .job(job)
                .jobSeeker(jobSeeker)
                .resume(resume)
                .coverLetter(coverLetter)
                .status("APPLIED")
                .build();

        Application savedApp = applicationRepository.save(application);
        System.out.println("DEBUG: Application saved with id=" + savedApp.getId());

        // Use direct query to find an employer for notification instead of accessing
        // lazy collection
        if (job.getCompany() != null) {
            employerRepository.findTopByCompanyId(job.getCompany().getId()).ifPresent(employer -> {
                System.out.println("DEBUG: Sending notification to employer: " + employer.getUser().getEmail());
                notificationService.sendNotification(employer.getUser(),
                        "New application received for " + job.getTitle());
            });
        }
        return savedApp;
    }

    @Override
    public List<Application> getApplicationsBySeeker(JobSeekerProfile jobSeeker) {
        return applicationRepository.findByJobSeeker(jobSeeker);
    }

    @Override
    public List<Application> getApplicationsByJob(Job job) {
        return applicationRepository.findByJob(job);
    }

    @Override
    public Application updateStatus(int applicationId, String status, String notes) {
        System.out.println("DEBUG: updateStatus started for app=" + applicationId + ", newStatus=" + status);
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        app.setStatus(status);
        if (notes != null) {
            app.setEmployerNotes(notes);
        }
        Application saved = applicationRepository.save(app);
        System.out.println("DEBUG: Application status updated in DB");

        try {
            notificationService.sendNotification(app.getJobSeeker().getUser(),
                    "Your application status for " + app.getJob().getTitle() + " has been updated to: " + status);
            System.out.println("DEBUG: Notification sent to seeker");
        } catch (Exception e) {
            System.out.println("DEBUG: Failed to send notification: " + e.getMessage());
        }
        return saved;
    }

    @Override
    public void withdrawApplication(int applicationId, String reason) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        app.setStatus("WITHDRAWN");
        app.setWithdrawReason(reason);
        applicationRepository.save(app);
    }

    @Override
    public void bulkUpdateStatus(List<Integer> applicationIds, String status, String notes) {
        for (Integer id : applicationIds) {
            updateStatus(id, status, notes);
        }
    }
}

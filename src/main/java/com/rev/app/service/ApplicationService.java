package com.rev.app.service;

import com.rev.app.entity.Application;
import com.rev.app.entity.Job;
import com.rev.app.entity.JobSeekerProfile;

import java.util.List;

public interface ApplicationService {
    Application applyForJob(Job job, JobSeekerProfile jobSeeker, String coverLetter);

    List<Application> getApplicationsBySeeker(JobSeekerProfile jobSeeker);

    List<Application> getApplicationsByJob(Job job);

    Application updateStatus(int applicationId, String status, String notes);

    void bulkUpdateStatus(List<Integer> applicationIds, String status, String notes);

    void withdrawApplication(int applicationId, String reason);
}

package com.rev.app.service;

import com.rev.app.entity.Job;
import com.rev.app.entity.User;
import java.util.List;

public interface JobService {
    List<Job> getAllOpenJobs();

    Job getJobById(int id);

    List<Job> searchJobs(String title, String location, String skills, Integer experience, String companyName,
            String jobType, String salaryRange, Integer daysSincePosted);

    Job createJob(Job job);

    Job updateJob(Job job);

    void deleteJob(int id);

    List<Job> getJobsByCompany(int companyId);

    void toggleFavorite(User user, int jobId);

    void updateJobStatus(int jobId, String status);
}

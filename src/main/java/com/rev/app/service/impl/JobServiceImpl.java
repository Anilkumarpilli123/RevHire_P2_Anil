package com.rev.app.service.impl;

import com.rev.app.entity.FavoriteJob;
import com.rev.app.entity.Job;
import com.rev.app.entity.User;
import com.rev.app.repository.FavoriteJobRepository;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.repository.JobRepository;
import com.rev.app.repository.JobSeekerRepository;
import com.rev.app.repository.ApplicationRepository;
import com.rev.app.service.JobService;
import com.rev.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private FavoriteJobRepository favoriteJobRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public List<Job> getAllOpenJobs() {
        return jobRepository.searchJobs(null, null, null, null, null, null);
    }

    @Override
    public Job getJobById(int id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @Override
    public List<Job> searchJobs(String title, String location, String skills, Integer experience, String companyName,
            String jobType) {
        return jobRepository.searchJobs(title, location, skills, experience, companyName, jobType);
    }

    @Override
    public Job createJob(Job job) {
        Job savedJob = jobRepository.save(job);

        // Notify matching job seekers
        try {
            List<JobSeekerProfile> seekers = jobSeekerRepository.findAll();
            for (JobSeekerProfile seeker : seekers) {
                if (isMatch(seeker, savedJob)) {
                    String message = String.format("New job posted from %s that matches you: %s",
                            savedJob.getCompany().getName(), savedJob.getTitle());
                    notificationService.sendNotification(seeker.getUser(), message);
                }
            }
        } catch (Exception e) {
            System.err.println("Error sending job notifications: " + e.getMessage());
        }

        return savedJob;
    }

    private boolean isMatch(JobSeekerProfile seeker, Job job) {
        if (seeker.getJobRole() == null || seeker.getJobRole().isEmpty())
            return false;

        String role = seeker.getJobRole().toLowerCase();
        String title = job.getTitle().toLowerCase();
        String skills = job.getSkillsRequired() != null ? job.getSkillsRequired().toLowerCase() : "";

        return title.contains(role) || skills.contains(role);
    }

    @Override
    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    @Transactional
    public void deleteJob(int id) {
        Job job = getJobById(id);
        // Delete associated applications first
        var applications = applicationRepository.findByJob(job);
        applicationRepository.deleteAll(applications);

        jobRepository.deleteById(id);
    }

    @Override
    public void updateJobStatus(int jobId, String status) {
        Job job = getJobById(jobId);
        job.setStatus(status);
        jobRepository.save(job);
    }

    @Override
    public List<Job> getJobsByCompany(int companyId) {
        return jobRepository.findByCompany_Id(companyId);
    }

    @Override
    @Transactional
    public void toggleFavorite(User user, int jobId) {
        Job job = getJobById(jobId);
        if (favoriteJobRepository.existsByUserAndJob(user, job)) {
            favoriteJobRepository.deleteByUserAndJob(user, job);
        } else {
            FavoriteJob fav = FavoriteJob.builder().user(user).job(job).build();
            favoriteJobRepository.save(fav);
        }
    }
}

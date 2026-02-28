package com.rev.app.service.impl;

import com.rev.app.entity.FavoriteJob;
import com.rev.app.entity.Job;
import com.rev.app.entity.User;
import com.rev.app.repository.FavoriteJobRepository;
import com.rev.app.repository.JobRepository;
import com.rev.app.service.JobService;
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
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(int id) {
        jobRepository.deleteById(id);
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

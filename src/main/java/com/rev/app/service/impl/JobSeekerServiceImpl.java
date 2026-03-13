package com.rev.app.service.impl;

import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.User;
import com.rev.app.repository.JobSeekerRepository;
import com.rev.app.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Override
    public JobSeekerProfile getProfileByUser(User user) {
        return jobSeekerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Override
    public JobSeekerProfile updateProfile(JobSeekerProfile profile) {
        return jobSeekerRepository.save(profile);
    }
}

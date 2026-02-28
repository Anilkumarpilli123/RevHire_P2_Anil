package com.rev.app.service;

import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.User;

public interface JobSeekerService {
    JobSeekerProfile getProfileByUser(User user);

    JobSeekerProfile updateProfile(JobSeekerProfile profile);
}

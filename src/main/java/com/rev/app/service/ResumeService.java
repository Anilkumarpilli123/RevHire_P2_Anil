package com.rev.app.service;

import com.rev.app.entity.Resume;
import com.rev.app.entity.JobSeekerProfile;

public interface ResumeService {
    Resume getResumeBySeeker(JobSeekerProfile seeker);

    Resume saveResume(Resume resume);

    String uploadResume(JobSeekerProfile seeker, org.springframework.web.multipart.MultipartFile file);

    void deleteResume(JobSeekerProfile seeker);
}

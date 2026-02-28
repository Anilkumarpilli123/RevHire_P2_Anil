package com.rev.app.repository;

import com.rev.app.entity.Application;
import com.rev.app.entity.Job;
import com.rev.app.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByJobSeeker(JobSeekerProfile jobSeeker);

    List<Application> findByJob(Job job);

    Boolean existsByJobAndJobSeeker(Job job, JobSeekerProfile jobSeeker);
}

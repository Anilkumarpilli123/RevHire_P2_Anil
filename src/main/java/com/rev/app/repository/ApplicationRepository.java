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

    @org.springframework.data.jpa.repository.Query("SELECT a FROM Application a WHERE a.job = :job AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:name IS NULL OR LOWER(a.jobSeeker.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:experience IS NULL OR a.jobSeeker.experience LIKE CONCAT('%', :experience, '%'))")
    List<Application> searchApplications(
            @org.springframework.data.repository.query.Param("job") Job job,
            @org.springframework.data.repository.query.Param("status") String status,
            @org.springframework.data.repository.query.Param("name") String name,
            @org.springframework.data.repository.query.Param("experience") String experience);

    Boolean existsByJobAndJobSeeker(Job job, JobSeekerProfile jobSeeker);
}

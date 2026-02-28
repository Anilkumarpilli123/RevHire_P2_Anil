package com.rev.app.repository;

import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeekerProfile, Integer> {
    Optional<JobSeekerProfile> findByUser(User user);
}

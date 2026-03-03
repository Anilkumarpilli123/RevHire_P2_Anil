package com.rev.app.repository;

import com.rev.app.entity.Resume;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ResumeRepositoryTest {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByJobSeeker_Success() {
        User user = userRepository.save(User.builder().email("s@s.com").password("p").role("S").build());
        JobSeekerProfile seeker = jobSeekerRepository.save(JobSeekerProfile.builder().user(user).name("S").build());
        Resume resume = Resume.builder().jobSeeker(seeker).resumePath("/tmp/r.pdf").build();
        resumeRepository.save(resume);

        Optional<Resume> found = resumeRepository.findByJobSeeker(seeker);
        assertTrue(found.isPresent());
    }
}

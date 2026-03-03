package com.rev.app.repository;

import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class JobSeekerRepositoryTest {

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUser_Success() {
        User user = userRepository.save(User.builder().email("s@s.com").password("p").role("S").build());
        JobSeekerProfile profile = JobSeekerProfile.builder().user(user).name("Seeker").build();
        jobSeekerRepository.save(profile);

        Optional<JobSeekerProfile> found = jobSeekerRepository.findByUser(user);
        assertTrue(found.isPresent());
        assertEquals("Seeker", found.get().getName());
    }
}

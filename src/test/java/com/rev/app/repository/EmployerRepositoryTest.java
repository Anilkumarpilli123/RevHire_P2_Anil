package com.rev.app.repository;

import com.rev.app.entity.EmployerProfile;
import com.rev.app.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployerRepositoryTest {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUser_Success() {
        User user = userRepository.save(User.builder().email("e@e.com").password("p").role("E").build());
        EmployerProfile profile = EmployerProfile.builder().user(user).name("Recruiter").build();
        employerRepository.save(profile);

        Optional<EmployerProfile> found = employerRepository.findByUser(user);
        assertTrue(found.isPresent());
        assertEquals("Recruiter", found.get().getName());
    }
}

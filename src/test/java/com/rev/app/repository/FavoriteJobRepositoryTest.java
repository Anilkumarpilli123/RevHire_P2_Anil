package com.rev.app.repository;

import com.rev.app.entity.FavoriteJob;
import com.rev.app.entity.Job;
import com.rev.app.entity.User;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FavoriteJobRepositoryTest {

    @Autowired
    private FavoriteJobRepository favoriteJobRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void findByUser_Success() {
        User user = userRepository.save(User.builder().email("f@e.com").password("p").role("S").build());
        Company company = companyRepository.save(Company.builder().name("C").build());
        Job job = jobRepository.save(Job.builder().title("J").company(company).build());

        FavoriteJob fav = FavoriteJob.builder().user(user).job(job).build();
        favoriteJobRepository.save(fav);

        List<FavoriteJob> favs = favoriteJobRepository.findByUser(user);
        assertEquals(1, favs.size());
    }

    @Test
    void findByUserAndJob_Success() {
        User user = userRepository.save(User.builder().email("f2@e.com").password("p").role("S").build());
        Company company = companyRepository.save(Company.builder().name("C").build());
        Job job = jobRepository.save(Job.builder().title("J").company(company).build());

        FavoriteJob fav = FavoriteJob.builder().user(user).job(job).build();
        favoriteJobRepository.save(fav);

        Optional<FavoriteJob> found = favoriteJobRepository.findByUserAndJob(user, job);
        assertTrue(found.isPresent());
    }
}

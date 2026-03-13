package com.rev.app.repository;

import com.rev.app.entity.Application;
import com.rev.app.entity.Job;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.Company;
import com.rev.app.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByJob_Success() {
        User user = userRepository.save(User.builder().email("s@s.com").password("p").role("S").build());
        Company company = companyRepository.save(Company.builder().name("C").build());
        Job job = jobRepository.save(Job.builder().title("J").company(company).build());
        JobSeekerProfile seeker = jobSeekerRepository.save(JobSeekerProfile.builder().user(user).name("S").build());

        Application app = Application.builder().job(job).jobSeeker(seeker).status("APPLIED").build();
        applicationRepository.save(app);

        List<Application> apps = applicationRepository.findByJob(job);
        assertEquals(1, apps.size());
    }
}

package com.rev.app.repository;

import com.rev.app.entity.Company;
import com.rev.app.entity.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void findByCompany_Id_Success() {
        Company company = Company.builder().name("Test Company").build();
        company = companyRepository.save(company);

        Job job = Job.builder()
                .title("Dev")
                .company(company)
                .status("OPEN")
                .deadline(LocalDate.now().plusDays(10))
                .build();
        jobRepository.save(job);

        List<Job> jobs = jobRepository.findByCompany_Id(company.getId());

        assertFalse(jobs.isEmpty());
        assertEquals(1, jobs.size());
    }

    @Test
    void findByStatus_Success() {
        Job job = Job.builder()
                .title("Dev")
                .company(Company.builder().name("C1").build())
                .status("OPEN")
                .build();
        jobRepository.save(job);

        List<Job> jobs = jobRepository.findByStatus("OPEN");
        assertFalse(jobs.isEmpty());
    }
}

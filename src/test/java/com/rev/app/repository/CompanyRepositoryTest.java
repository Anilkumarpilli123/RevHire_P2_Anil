package com.rev.app.repository;

import com.rev.app.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void findByName_Success() {
        Company c = Company.builder().name("Tech").build();
        companyRepository.save(c);

        Optional<Company> found = companyRepository.findByName("Tech");
        assertTrue(found.isPresent());
    }
}

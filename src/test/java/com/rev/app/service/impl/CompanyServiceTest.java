package com.rev.app.service.impl;

import com.rev.app.entity.Company;
import com.rev.app.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company testCompany;

    @BeforeEach
    void setUp() {
        testCompany = Company.builder().id(1).name("Test Corp").build();
    }

    @Test
    void getCompanyById_Success() {
        when(companyRepository.findById(1)).thenReturn(Optional.of(testCompany));

        Company found = companyService.getCompanyById(1);

        assertNotNull(found);
        assertEquals("Test Corp", found.getName());
    }

    @Test
    void updateCompany_Success() {
        when(companyRepository.save(any(Company.class))).thenReturn(testCompany);

        Company updated = companyService.updateCompany(testCompany);

        assertNotNull(updated);
        verify(companyRepository, times(1)).save(testCompany);
    }
}

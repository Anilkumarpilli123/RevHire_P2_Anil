package com.rev.app.service;

import com.rev.app.entity.Company;
import java.util.List;

public interface CompanyService {
    Company createCompany(Company company);

    Company updateCompany(Company company);

    Company getCompanyById(int id);

    List<Company> getAllCompanies();
}

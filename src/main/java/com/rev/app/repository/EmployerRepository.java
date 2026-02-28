package com.rev.app.repository;

import com.rev.app.entity.EmployerProfile;
import com.rev.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<EmployerProfile, Integer> {
    Optional<EmployerProfile> findByUser(User user);

    Optional<EmployerProfile> findTopByCompanyId(int companyId);
}

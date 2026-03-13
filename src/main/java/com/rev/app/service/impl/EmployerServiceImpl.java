package com.rev.app.service.impl;

import com.rev.app.entity.EmployerProfile;
import com.rev.app.entity.User;
import com.rev.app.repository.EmployerRepository;
import com.rev.app.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployerServiceImpl implements EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public EmployerProfile getProfileByUser(User user) {
        return employerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employer profile not found"));
    }

    @Override
    public EmployerProfile updateProfile(EmployerProfile profile) {
        return employerRepository.save(profile);
    }
}

package com.rev.app.service;

import com.rev.app.entity.EmployerProfile;
import com.rev.app.entity.User;

public interface EmployerService {
    EmployerProfile getProfileByUser(User user);

    EmployerProfile updateProfile(EmployerProfile profile);
}

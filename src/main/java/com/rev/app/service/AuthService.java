package com.rev.app.service;

import com.rev.app.dto.JwtResponse;
import com.rev.app.dto.LoginRequest;
import com.rev.app.dto.SignupRequest;
import com.rev.app.entity.User;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);

    User registerUser(SignupRequest signupRequest);
}

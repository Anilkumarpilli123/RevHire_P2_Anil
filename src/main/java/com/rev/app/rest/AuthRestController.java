package com.rev.app.rest;

import com.rev.app.dto.JwtResponse;
import com.rev.app.dto.UserDto;
import com.rev.app.entity.User;
import com.rev.app.mapper.UserMapper;
import com.rev.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthRestController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        User user = authService.registerUser(signupRequest);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", userMapper.toDto(user)));
    }
}

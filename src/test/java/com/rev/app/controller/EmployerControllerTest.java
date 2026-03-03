package com.rev.app.controller;

import com.rev.app.entity.Company;
import com.rev.app.entity.EmployerProfile;
import com.rev.app.entity.User;
import com.rev.app.service.EmployerService;
import com.rev.app.service.JobService;
import com.rev.app.service.ApplicationService;
import com.rev.app.service.NotificationService;
import com.rev.app.repository.UserRepository;
import com.rev.app.config.JwtUtils;
import com.rev.app.config.JwtAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployerController.class)
class EmployerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployerService employerService;

    @MockitoBean
    private JobService jobService;

    @MockitoBean
    private ApplicationService applicationService;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() throws Exception {
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser(username = "employer@e.com", roles = "EMPLOYER")
    void dashboard_Success() throws Exception {
        User user = User.builder().id(1).email("employer@e.com").role("EMPLOYER").build();
        Company company = Company.builder().id(1).name("Comp").industry("IT").location("Loc").build();
        EmployerProfile profile = EmployerProfile.builder()
                .name("E").phone("123").location("Loc").jobRole("Dev")
                .company(company).build();
        when(userRepository.findByEmail("employer@e.com")).thenReturn(Optional.of(user));
        when(employerService.getProfileByUser(user)).thenReturn(profile);

        mockMvc.perform(get("/employer/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("employer/dashboard"));
    }
}

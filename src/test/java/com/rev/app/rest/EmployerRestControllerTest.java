package com.rev.app.rest;

import com.rev.app.dto.ApiResponse;
import com.rev.app.dto.JobDto;
import com.rev.app.entity.Company;
import com.rev.app.entity.EmployerProfile;
import com.rev.app.entity.Job;
import com.rev.app.entity.User;
import com.rev.app.mapper.JobMapper;
import com.rev.app.mapper.ApplicationMapper;
import com.rev.app.repository.UserRepository;
import com.rev.app.service.JobService;
import com.rev.app.service.EmployerService;
import com.rev.app.service.ApplicationService;
import com.rev.app.service.AuthService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(EmployerRestController.class)
class EmployerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    @MockitoBean
    private EmployerService employerService;

    @MockitoBean
    private ApplicationService applicationService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JobMapper jobMapper;

    @MockitoBean
    private ApplicationMapper applicationMapper;

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
    void getAllJobs_Success() throws Exception {
        User user = User.builder().email("employer@e.com").role("EMPLOYER").build();
        Company company = Company.builder().id(1).name("Comp").build();
        EmployerProfile profile = EmployerProfile.builder().company(company).build();

        when(userRepository.findByEmail("employer@e.com")).thenReturn(Optional.of(user));
        when(employerService.getProfileByUser(user)).thenReturn(profile);
        when(jobService.getJobsByCompany(1)).thenReturn(Arrays.asList(
                Job.builder().title("Dev").build()));

        mockMvc.perform(get("/api/employer/jobs"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "employer@e.com", roles = "EMPLOYER")
    void createJob_Success() throws Exception {
        User user = User.builder().email("employer@e.com").role("EMPLOYER").build();
        Company company = Company.builder().id(1).name("Comp").build();
        EmployerProfile profile = EmployerProfile.builder().company(company).build();

        when(userRepository.findByEmail("employer@e.com")).thenReturn(Optional.of(user));
        when(employerService.getProfileByUser(user)).thenReturn(profile);

        Job job = Job.builder().title("New Job").build();
        when(jobService.createJob(any(Job.class))).thenReturn(job);

        mockMvc.perform(post("/api/employer/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Job\"}")
                .with(csrf()))
                .andExpect(status().isOk());
    }
}

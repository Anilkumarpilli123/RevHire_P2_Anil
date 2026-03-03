package com.rev.app.rest;

import com.rev.app.dto.JobDto;
import com.rev.app.entity.Job;
import com.rev.app.mapper.JobMapper;
import com.rev.app.mapper.JobSeekerProfileMapper;
import com.rev.app.mapper.ResumeMapper;
import com.rev.app.repository.UserRepository;
import com.rev.app.service.JobSeekerService;
import com.rev.app.service.JobService;
import com.rev.app.service.ResumeService;
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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeekerRestController.class)
class SeekerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    @MockitoBean
    private JobSeekerService jobSeekerService;

    @MockitoBean
    private ResumeService resumeService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JobMapper jobMapper;

    @MockitoBean
    private JobSeekerProfileMapper profileMapper;

    @MockitoBean
    private ResumeMapper resumeMapper;

    @MockitoBean
    private UserRepository userRepository;

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
    @WithMockUser(roles = "SEEKER")
    void searchJobs_Success() throws Exception {
        when(jobService.searchJobs("Java", null, null, null, null, null)).thenReturn(Arrays.asList(
                Job.builder().title("Java Dev").build()));
        when(jobMapper.toDto(any())).thenReturn(JobDto.builder().title("Java Dev").build());

        mockMvc.perform(get("/api/seeker/jobs/search?title=Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Dev"));
    }
}

package com.rev.app.controller;

import com.rev.app.entity.EmploymentStatus;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.User;
import com.rev.app.service.JobSeekerService;
import com.rev.app.service.JobService;
import com.rev.app.service.ApplicationService;
import com.rev.app.service.ResumeService;
import com.rev.app.service.NotificationService;
import com.rev.app.repository.UserRepository;
import com.rev.app.repository.FavoriteJobRepository;
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

@WebMvcTest(SeekerController.class)
class SeekerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobSeekerService jobSeekerService;

    @MockitoBean
    private JobService jobService;

    @MockitoBean
    private ApplicationService applicationService;

    @MockitoBean
    private ResumeService resumeService;

    @MockitoBean
    private FavoriteJobRepository favoriteJobRepository;

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
    @WithMockUser(username = "seeker@s.com", roles = "SEEKER")
    void dashboard_Success() throws Exception {
        User user = User.builder().id(1).email("seeker@s.com").role("SEEKER").build();
        JobSeekerProfile profile = JobSeekerProfile.builder()
                .name("S").phone("123").location("Loc")
                .employmentStatus(EmploymentStatus.UNEMPLOYED)
                .experience("Fresher").jobRole("Dev").build();
        when(userRepository.findByEmail("seeker@s.com")).thenReturn(Optional.of(user));
        when(jobSeekerService.getProfileByUser(user)).thenReturn(profile);

        mockMvc.perform(get("/seeker/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("seeker/dashboard"));
    }
}

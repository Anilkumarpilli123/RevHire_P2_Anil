package com.rev.app.rest;

import com.rev.app.dto.ApiResponse;
import com.rev.app.dto.ApplicationDto;
import com.rev.app.dto.JobDto;
import com.rev.app.entity.*;
import com.rev.app.mapper.ApplicationMapper;
import com.rev.app.mapper.JobMapper;
import com.rev.app.repository.UserRepository;
import com.rev.app.service.ApplicationService;
import com.rev.app.service.EmployerService;
import com.rev.app.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employer")
public class EmployerRestController {

    @Autowired
    private JobService jobService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<JobDto>>> getMyJobs(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        EmployerProfile profile = employerService.getProfileByUser(user);
        if (profile.getCompany() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("No company linked to your profile"));
        }
        List<JobDto> jobs = jobService.getJobsByCompany(profile.getCompany().getId())
                .stream().map(jobMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Jobs fetched successfully", jobs));
    }

    @PostMapping("/jobs")
    public ResponseEntity<ApiResponse<JobDto>> createJob(Authentication auth, @RequestBody Job job) {
        User user = userRepository.findByEmail(auth.getName()).get();
        EmployerProfile profile = employerService.getProfileByUser(user);
        job.setCompany(profile.getCompany());
        Job saved = jobService.createJob(job);
        return ResponseEntity.ok(ApiResponse.success("Job posted successfully", jobMapper.toDto(saved)));
    }

    @GetMapping("/applicants/{jobId}")
    public ResponseEntity<ApiResponse<List<ApplicationDto>>> getApplicants(@PathVariable int jobId) {
        Job job = jobService.getJobById(jobId);
        List<ApplicationDto> applications = applicationService.getApplicationsByJob(job)
                .stream().map(applicationMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Applicants fetched successfully", applications));
    }

    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<ApiResponse<ApplicationDto>> updateApplicationStatus(
            @PathVariable int applicationId, @RequestParam String status) {
        Application updated = applicationService.updateStatus(applicationId, status, null);
        return ResponseEntity.ok(ApiResponse.success("Application status updated", applicationMapper.toDto(updated)));
    }
}

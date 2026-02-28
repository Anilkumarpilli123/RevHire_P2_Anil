package com.rev.app.rest;

import com.rev.app.dto.ApiResponse;
import com.rev.app.dto.JobDto;
import com.rev.app.dto.JobSeekerProfileDto;
import com.rev.app.dto.ResumeDto;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.Resume;
import com.rev.app.entity.User;
import com.rev.app.mapper.JobMapper;
import com.rev.app.mapper.JobSeekerProfileMapper;
import com.rev.app.mapper.ResumeMapper;
import com.rev.app.mapper.UserMapper;
import com.rev.app.repository.UserRepository;
import com.rev.app.service.JobSeekerService;
import com.rev.app.service.JobService;
import com.rev.app.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seeker")
public class SeekerRestController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobSeekerProfileMapper profileMapper;

    @Autowired
    private ResumeMapper resumeMapper;

    @GetMapping("/jobs/search")
    public List<JobDto> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) Integer experience) {
        return jobService.searchJobs(title, location, skills, experience, null, null)
                .stream().map(jobMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<JobSeekerProfileDto>> getProfile(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = jobSeekerService.getProfileByUser(user);
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", profileMapper.toDto(profile)));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<JobSeekerProfileDto>> updateProfile(Authentication auth,
                                                                          @RequestBody JobSeekerProfile profile) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile existing = jobSeekerService.getProfileByUser(user);
        profile.setId(existing.getId());
        profile.setUser(user);
        JobSeekerProfile updated = jobSeekerService.updateProfile(profile);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profileMapper.toDto(updated)));
    }

    @GetMapping("/resume")
    public ResponseEntity<ApiResponse<ResumeDto>> getResume(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = jobSeekerService.getProfileByUser(user);
        Resume resume = resumeService.getResumeBySeeker(profile);
        return ResponseEntity.ok(ApiResponse.success("Resume fetched successfully", resumeMapper.toDto(resume)));
    }

    @PostMapping("/resume")
    public ResponseEntity<ApiResponse<ResumeDto>> saveResume(Authentication auth, @RequestBody Resume resume) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = jobSeekerService.getProfileByUser(user);
        resume.setJobSeeker(profile);
        Resume saved = resumeService.saveResume(resume);
        return ResponseEntity.ok(ApiResponse.success("Resume saved successfully", resumeMapper.toDto(saved)));
    }
}

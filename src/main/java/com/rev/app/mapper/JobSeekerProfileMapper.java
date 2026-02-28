package com.rev.app.mapper;

import com.rev.app.dto.JobSeekerProfileDto;
import com.rev.app.entity.JobSeekerProfile;
import org.springframework.stereotype.Component;

@Component
public class JobSeekerProfileMapper {
    public JobSeekerProfileDto toDto(JobSeekerProfile profile) {
        if (profile == null)
            return null;
        return JobSeekerProfileDto.builder()
                .id(profile.getId())
                .name(profile.getName())
                .email(profile.getUser() != null ? profile.getUser().getEmail() : null)
                .phone(profile.getPhone())
                .location(profile.getLocation())
                .employmentStatus(profile.getEmploymentStatus() != null ? profile.getEmploymentStatus().name() : null)
                .companyName(profile.getCompanyName())
                .collegeName(profile.getCollegeName())
                .branch(profile.getBranch())
                .jobRole(profile.getJobRole())
                .experience(profile.getExperience())
                .experienceYears(profile.getExperienceYears())
                .profileCompletion(profile.getProfileCompletion())
                .build();
    }
}

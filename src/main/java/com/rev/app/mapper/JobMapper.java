package com.rev.app.mapper;

import com.rev.app.dto.JobDto;
import com.rev.app.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {
    public JobDto toDto(Job job) {
        if (job == null)
            return null;
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .companyName(job.getCompany() != null ? job.getCompany().getName() : null)
                .description(job.getDescription())
                .skillsRequired(job.getSkillsRequired())
                .experienceRequired(job.getExperienceRequired())
                .location(job.getLocation())
                .salaryRange(job.getSalaryRange())
                .jobType(job.getJobType())
                .status(job.getStatus())
                .createdAt(job.getCreatedAt())
                .build();
    }
}

package com.rev.app.mapper;

import com.rev.app.dto.ApplicationDto;
import com.rev.app.entity.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {
    public ApplicationDto toDto(Application app) {
        if (app == null)
            return null;
        return ApplicationDto.builder()
                .id(app.getId())
                .jobId(app.getJob().getId())
                .jobTitle(app.getJob().getTitle())
                .seekerName(app.getJobSeeker().getName())
                .status(app.getStatus())
                .appliedDate(app.getAppliedDate())
                .employerNotes(app.getEmployerNotes())
                .build();
    }
}

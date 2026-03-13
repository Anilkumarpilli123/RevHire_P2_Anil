package com.rev.app.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {
    private int id;
    private String title;
    private String companyName;
    private String description;
    private String skillsRequired;
    private Integer experienceRequired;
    private String location;
    private String salaryRange;
    private String jobType;
    private String status;
    private LocalDateTime createdAt;
}

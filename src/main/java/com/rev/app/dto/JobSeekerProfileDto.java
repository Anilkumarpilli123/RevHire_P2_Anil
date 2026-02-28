package com.rev.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSeekerProfileDto {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String location;
    private String employmentStatus;
    private String companyName;
    private String collegeName;
    private String branch;
    private String jobRole;
    private String experience;
    private Integer experienceYears;
    private Integer profileCompletion;
}

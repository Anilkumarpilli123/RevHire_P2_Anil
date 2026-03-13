package com.rev.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeDto {
    private int id;
    private String objective;
    private String education;
    private String experience;
    private String skills;
    private String projects;
    private String certifications;
    private String resumePath;
}

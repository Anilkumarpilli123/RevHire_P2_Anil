package com.rev.app.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDto {
    private int id;
    private int jobId;
    private String jobTitle;
    private String seekerName;
    private String status;
    private LocalDateTime appliedDate;
    private String employerNotes;
}

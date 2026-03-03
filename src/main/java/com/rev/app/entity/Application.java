package com.rev.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "job", "jobSeeker", "resume" })
@EqualsAndHashCode(exclude = { "job", "jobSeeker", "resume" })
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_seq")
    @SequenceGenerator(name = "application_seq", sequenceName = "APPLICATION_SEQ", allocationSize = 1)
    @Column(name = "application_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeekerProfile jobSeeker;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Column(name = "cover_letter", columnDefinition = "CLOB")
    private String coverLetter;

    @Builder.Default
    private String status = "APPLIED"; // APPLIED, UNDER_REVIEW, SHORTLISTED, REJECTED, WITHDRAWN

    @CreationTimestamp
    @Column(name = "applied_date", updatable = false)
    private LocalDateTime appliedDate;

    @Column(name = "withdraw_reason")
    private String withdrawReason;

    @Column(name = "employer_notes", columnDefinition = "CLOB")
    private String employerNotes;
}

package com.rev.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "JOBS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "company")
@EqualsAndHashCode(exclude = "company")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_seq")
    @SequenceGenerator(name = "job_seq", sequenceName = "JOB_SEQ", allocationSize = 1)
    @Column(name = "job_id")
    private int id;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "CLOB")
    private String description;

    @Column(name = "skills_required", columnDefinition = "CLOB")
    private String skillsRequired;

    @Column(name = "experience_required")
    private Integer experienceRequired;

    @Column(name = "education_required")
    private String educationRequired;

    private String location;

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "job_type")
    private String jobType;

    private LocalDate deadline;

    @Builder.Default
    private String status = "OPEN"; // OPEN, CLOSED, FILLED

    @Column(name = "num_openings")
    @Builder.Default
    private Integer numberOfOpenings = 1;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

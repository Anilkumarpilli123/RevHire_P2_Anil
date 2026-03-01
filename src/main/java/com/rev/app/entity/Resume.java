package com.rev.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RESUMES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "jobSeeker")
@EqualsAndHashCode(exclude = "jobSeeker")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resume_seq")
    @SequenceGenerator(name = "resume_seq", sequenceName = "RESUME_SEQ", allocationSize = 1)
    @Column(name = "resume_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeekerProfile jobSeeker;

    @Column(columnDefinition = "CLOB")
    private String objective;

    @Column(columnDefinition = "CLOB")
    private String education;

    @Column(columnDefinition = "CLOB")
    private String experience;

    @Column(columnDefinition = "CLOB")
    private String skills;

    @Column(columnDefinition = "CLOB")
    private String projects;

    @Column(columnDefinition = "CLOB")
    private String certifications;

    @Column(name = "resume_path")
    private String resumePath;
}

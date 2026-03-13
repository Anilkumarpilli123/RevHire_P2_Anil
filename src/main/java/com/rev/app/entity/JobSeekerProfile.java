package com.rev.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_seekers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "user", "resume" })
@EqualsAndHashCode(exclude = { "user", "resume" })
public class JobSeekerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_seeker_seq")
    @SequenceGenerator(name = "job_seeker_seq", sequenceName = "JOB_SEEKER_SEQ", allocationSize = 1)
    @Column(name = "job_seeker_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "jobSeeker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Resume resume;

    @Column(nullable = false)
    private String name;

    private String phone;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "college_name")
    private String collegeName;

    private String branch;

    @Column(name = "job_role")
    private String jobRole;

    private String experience;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "profile_completion")
    @Builder.Default
    private Integer profileCompletion = 0;

    public boolean isComplete() {
        boolean basic = name != null && !name.trim().isEmpty() &&
                phone != null && !phone.trim().isEmpty() &&
                location != null && !location.trim().isEmpty() &&
                employmentStatus != null;

        if (!basic)
            return false;

        if (EmploymentStatus.EMPLOYED.equals(employmentStatus)) {
            return companyName != null && !companyName.trim().isEmpty() &&
                    jobRole != null && !jobRole.trim().isEmpty() &&
                    experience != null && !experience.trim().isEmpty();
        } else if (EmploymentStatus.UNEMPLOYED.equals(employmentStatus)) {
            if ("Fresher".equalsIgnoreCase(experience)) {
                return jobRole != null && !jobRole.trim().isEmpty();
            }
            return experience != null && !experience.trim().isEmpty();
        } else if (EmploymentStatus.STUDENT.equals(employmentStatus)) {
            return collegeName != null && !collegeName.trim().isEmpty() &&
                    branch != null && !branch.trim().isEmpty() &&
                    experience != null && !experience.trim().isEmpty();
        }
        return true;
    }
}

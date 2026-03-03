package com.rev.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EMPLOYERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "company")
@EqualsAndHashCode(exclude = "company")
public class EmployerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employer_seq")
    @SequenceGenerator(name = "employer_seq", sequenceName = "EMPLOYER_SEQ", allocationSize = 1)
    @Column(name = "employer_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "company_id")
    private Company company;

    private String name;
    private String phone;
    private String location;

    @Column(name = "job_role")
    private String jobRole;

    public boolean isComplete() {
        return name != null && !name.trim().isEmpty() &&
                phone != null && !phone.trim().isEmpty() &&
                location != null && !location.trim().isEmpty() &&
                jobRole != null && !jobRole.trim().isEmpty() &&
                company != null && company.getName() != null && !company.getName().trim().isEmpty() &&
                company.getIndustry() != null && !company.getIndustry().trim().isEmpty() &&
                company.getLocation() != null && !company.getLocation().trim().isEmpty();
    }
}

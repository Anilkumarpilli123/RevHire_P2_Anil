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
}

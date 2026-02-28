package com.rev.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COMPANIES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "employerProfiles")
@EqualsAndHashCode(exclude = "employerProfiles")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
    @SequenceGenerator(name = "company_seq", sequenceName = "COMPANY_SEQ", allocationSize = 1)
    @Column(name = "company_id")
    private int id;

    @Column(nullable = false)
    private String name;

    private String industry;

    @Column(name = "company_size")
    private String size;

    @Column(columnDefinition = "CLOB")
    private String description;

    private String website;
    private String location;

    @OneToMany(mappedBy = "company")
    @Builder.Default
    private java.util.Set<EmployerProfile> employerProfiles = new java.util.HashSet<>();
}

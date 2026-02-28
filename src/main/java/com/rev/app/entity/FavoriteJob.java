package com.rev.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "FAVORITE_JOBS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "user", "job" })
@EqualsAndHashCode(exclude = { "user", "job" })
public class FavoriteJob {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fav_job_seq")
    @SequenceGenerator(name = "fav_job_seq", sequenceName = "FAV_JOB_SEQ", allocationSize = 1)
    @Column(name = "favorite_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @CreationTimestamp
    @Column(name = "saved_at", updatable = false)
    private LocalDateTime savedAt;
}

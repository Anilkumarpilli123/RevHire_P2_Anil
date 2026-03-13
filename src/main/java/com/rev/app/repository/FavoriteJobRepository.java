package com.rev.app.repository;

import com.rev.app.entity.FavoriteJob;
import com.rev.app.entity.Job;
import com.rev.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteJobRepository extends JpaRepository<FavoriteJob, Integer> {
    List<FavoriteJob> findByUser(User user);

    Optional<FavoriteJob> findByUserAndJob(User user, Job job);

    boolean existsByUserAndJob(User user, Job job);

    @Transactional
    @Modifying
    void deleteByUserAndJob(User user, Job job);
}

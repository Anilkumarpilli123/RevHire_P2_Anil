package com.rev.app.repository;

import com.rev.app.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

        @Query("SELECT j FROM Job j WHERE " +
                        "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
                        "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
                        "(:skills IS NULL OR LOWER(j.skillsRequired) LIKE LOWER(CONCAT('%', :skills, '%'))) AND " +
                        "(:experience IS NULL OR j.experienceRequired <= :experience) AND " +
                        "(:companyName IS NULL OR LOWER(j.company.name) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND "
                        +
                        "(:jobType IS NULL OR LOWER(j.jobType) = LOWER(:jobType)) AND " +
                        "(:salaryRange IS NULL OR LOWER(j.salaryRange) LIKE LOWER(CONCAT('%', :salaryRange, '%'))) AND "
                        +
                        "(:postedSince IS NULL OR j.createdAt >= :postedSince) AND " +
                        "(j.status = 'OPEN')")
        List<Job> searchJobs(@Param("title") String title,
                        @Param("location") String location,
                        @Param("skills") String skills,
                        @Param("experience") Integer experience,
                        @Param("companyName") String companyName,
                        @Param("jobType") String jobType,
                        @Param("salaryRange") String salaryRange,
                        @Param("postedSince") java.time.LocalDateTime postedSince);

        List<Job> findByCompany_Id(int companyId);

        List<Job> findByStatus(String status);
}

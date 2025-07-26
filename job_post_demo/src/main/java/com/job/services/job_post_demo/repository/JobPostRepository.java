package com.job.services.job_post_demo.repository;

import com.job.services.job_post_demo.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Integer> {
    List<JobPost> findByLocation(String location);

    List<JobPost> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(String title, String description, String location);
    // This interface will automatically provide CRUD operations for JobPost entity
    // No need to define any methods unless you want custom queries
}

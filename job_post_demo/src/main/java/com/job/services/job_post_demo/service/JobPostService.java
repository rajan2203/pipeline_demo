package com.job.services.job_post_demo.service;

import com.job.services.job_post_demo.model.JobPost;
import com.job.services.job_post_demo.repository.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobPostService {

    @Autowired
    JobPostRepository jobPostRepository;

    public List<JobPost> getAllJobPosts() {
        return jobPostRepository.findAll();
    }

    public JobPost addJobPost(JobPost jobPost) {
        return jobPostRepository.save(jobPost);
    }

    public List<JobPost> findByLocation(String location) {
        return jobPostRepository.findByLocation(location);
    }

    public List<JobPost> getJobsBySearchText(String text) {
        return jobPostRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(text, text, text);
    }
}

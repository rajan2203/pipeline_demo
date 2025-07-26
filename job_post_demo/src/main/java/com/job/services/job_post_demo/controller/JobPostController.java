package com.job.services.job_post_demo.controller;

import com.job.services.job_post_demo.model.JobPost;
import com.job.services.job_post_demo.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobPostController {

    @Autowired
    JobPostService jobPostService;

    @GetMapping("/jobposts")
//    @CrossOrigin // Either allow the cross origin error by this annotation or use a config to do that
    public List<JobPost> getAllJobPosts() {
        return jobPostService.getAllJobPosts();
    }

    @PostMapping("/jobposts/add")
    public JobPost addJobPost(@RequestBody JobPost jobPost) {
        // Assuming the service method handles saving the job post
        return jobPostService.addJobPost(jobPost);
    }

    @GetMapping("/jobposts/location/{location}")
    public List<JobPost> getPostsByLocation(@PathVariable String location){
        return jobPostService.findByLocation(location);
    }

    @GetMapping("/jobposts/search/{text}")
    public List<JobPost> searchPostByText(@PathVariable String text){
        return jobPostService.getJobsBySearchText(text);
    }
}

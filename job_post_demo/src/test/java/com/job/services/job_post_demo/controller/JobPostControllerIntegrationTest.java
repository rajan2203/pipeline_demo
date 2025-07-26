package com.job.services.job_post_demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.services.job_post_demo.model.JobPost;
import com.job.services.job_post_demo.repository.JobPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Transactional
class JobPostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Clear the repository and add test data
        jobPostRepository.deleteAll();
        
        JobPost job1 = new JobPost();
        job1.setTitle("Software Engineer");
        job1.setDescription("Java Developer position");
        job1.setLocation("New York");
        jobPostRepository.save(job1);
        
        JobPost job2 = new JobPost();
        job2.setTitle("Data Scientist");
        job2.setDescription("Machine Learning role");
        job2.setLocation("San Francisco");
        jobPostRepository.save(job2);
    }

    @Test
    void getAllJobPosts_ShouldReturnAllJobPostsFromDatabase() throws Exception {
        mockMvc.perform(get("/jobposts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[1].title").exists());
    }

    @Test
    void addJobPost_ShouldPersistJobPostToDatabase() throws Exception {
        JobPost newJobPost = new JobPost();
        newJobPost.setTitle("Frontend Developer");
        newJobPost.setDescription("React Developer position");
        newJobPost.setLocation("Boston");

        mockMvc.perform(post("/jobposts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newJobPost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Frontend Developer"))
                .andExpect(jsonPath("$.description").value("React Developer position"))
                .andExpect(jsonPath("$.location").value("Boston"));

        // Verify it was actually saved to the database
        mockMvc.perform(get("/jobposts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void getPostsByLocation_ShouldReturnJobPostsFromSpecificLocation() throws Exception {
        mockMvc.perform(get("/jobposts/location/New York"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].location").value("New York"));
    }

    @Test
    void searchPostByText_ShouldReturnMatchingJobPosts() throws Exception {
        mockMvc.perform(get("/jobposts/search/Java"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Java Developer position"));
    }
}

package com.job.services.job_post_demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.services.job_post_demo.model.JobPost;
import com.job.services.job_post_demo.service.JobPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobPostController.class)
class JobPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobPostService jobPostService;

    @Autowired
    private ObjectMapper objectMapper;

    private JobPost jobPost1;
    private JobPost jobPost2;
    private JobPost jobPost3;
    private List<JobPost> jobPosts;

    @BeforeEach
    void setUp() {
        jobPost1 = new JobPost(1, "Software Engineer", "Java Developer position", "New York");
        jobPost2 = new JobPost(2, "Data Scientist", "Machine Learning role", "San Francisco");
        jobPost3 = new JobPost(3, "Backend Developer", "Spring Boot developer", "New York");
        jobPosts = Arrays.asList(jobPost1, jobPost2, jobPost3);
    }

    @Test
    void getAllJobPosts_ShouldReturnAllJobPosts() throws Exception {
        // Given
        when(jobPostService.getAllJobPosts()).thenReturn(jobPosts);

        // When & Then
        mockMvc.perform(get("/jobposts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Software Engineer"))
                .andExpect(jsonPath("$[0].description").value("Java Developer position"))
                .andExpect(jsonPath("$[0].location").value("New York"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Data Scientist"))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].title").value("Backend Developer"));
    }

    @Test
    void getAllJobPosts_WhenNoJobPosts_ShouldReturnEmptyList() throws Exception {
        // Given
        when(jobPostService.getAllJobPosts()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/jobposts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void addJobPost_ShouldCreateNewJobPost() throws Exception {
        // Given
        JobPost newJobPost = new JobPost(0, "Frontend Developer", "React Developer position", "Boston");
        JobPost savedJobPost = new JobPost(4, "Frontend Developer", "React Developer position", "Boston");
        
        when(jobPostService.addJobPost(any(JobPost.class))).thenReturn(savedJobPost);

        // When & Then
        mockMvc.perform(post("/jobposts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newJobPost)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.title").value("Frontend Developer"))
                .andExpect(jsonPath("$.description").value("React Developer position"))
                .andExpect(jsonPath("$.location").value("Boston"));
    }

    @Test
    void addJobPost_WithInvalidData_ShouldHandleGracefully() throws Exception {
        // Given
        String invalidJson = "{\"title\":}"; // Invalid JSON

        // When & Then
        mockMvc.perform(post("/jobposts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPostsByLocation_ShouldReturnJobPostsForGivenLocation() throws Exception {
        // Given
        List<JobPost> newYorkJobs = Arrays.asList(jobPost1, jobPost3);
        when(jobPostService.findByLocation("New York")).thenReturn(newYorkJobs);

        // When & Then
        mockMvc.perform(get("/jobposts/location/New York"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].location").value("New York"))
                .andExpect(jsonPath("$[1].location").value("New York"));
    }

    @Test
    void getPostsByLocation_WhenNoJobsFound_ShouldReturnEmptyList() throws Exception {
        // Given
        when(jobPostService.findByLocation("Chicago")).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/jobposts/location/Chicago"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getPostsByLocation_WithSpecialCharacters_ShouldHandleCorrectly() throws Exception {
        // Given
        List<JobPost> jobs = Arrays.asList(jobPost1);
        when(jobPostService.findByLocation(anyString())).thenReturn(jobs);

        // When & Then
        mockMvc.perform(get("/jobposts/location/San Francisco"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void searchPostByText_ShouldReturnMatchingJobPosts() throws Exception {
        // Given
        List<JobPost> searchResults = Arrays.asList(jobPost1, jobPost3);
        when(jobPostService.getJobsBySearchText("Java")).thenReturn(searchResults);

        // When & Then
        mockMvc.perform(get("/jobposts/search/Java"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Software Engineer"))
                .andExpect(jsonPath("$[1].title").value("Backend Developer"));
    }

    @Test
    void searchPostByText_WhenNoMatches_ShouldReturnEmptyList() throws Exception {
        // Given
        when(jobPostService.getJobsBySearchText("NonExistentTerm")).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/jobposts/search/NonExistentTerm"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void searchPostByText_WithEmptyString_ShouldHandleCorrectly() throws Exception {
        // Given
        when(jobPostService.getJobsBySearchText(" ")).thenReturn(jobPosts);

        // When & Then - Using a space character instead of empty string to avoid routing issues
        mockMvc.perform(get("/jobposts/search/ "))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void searchPostByText_WithSpecialCharacters_ShouldHandleCorrectly() throws Exception {
        // Given
        when(jobPostService.getJobsBySearchText("C++")).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/jobposts/search/C++"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllEndpoints_ShouldHaveCorrectHttpMethods() throws Exception {
        // Test that POST to /jobposts returns method not allowed
        mockMvc.perform(post("/jobposts"))
                .andExpect(status().isMethodNotAllowed());

        // Test that GET to /jobposts/add returns method not allowed
        mockMvc.perform(get("/jobposts/add"))
                .andExpect(status().isMethodNotAllowed());
    }
}

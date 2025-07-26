package com.job.services.job_post_demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

@OpenAPIDefinition(
		info = @Info(
				title = "My Job Post API",
				version = "1.0",
				description = "API for managing job posts"
		)
)

public class JobPostDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(JobPostDemoApplication.class, args);
	}
}

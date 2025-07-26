package com.job.services.job_post_demo.aspect;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect { // We are not supposed to write other code like logging within the business logic
    // Thus we are making it outside of business logic with the help of Aspect


    // We are mentioning at what point this log to be printed/called and in which method call this should be called to be given inside the execution
    // The * here represents the wildcard reference, we have to provide the fully qualified name for the method name package.class.method.
    @Before("execution(public * com.job.services.job_post_demo.controller.JobPostController.getAllJobPosts())")
    public void logBefore(){
        System.out.println("Before get posts method called Logging...");
    }

    // This is the finally after which will print even the exception is throws
    @After("execution(public * com.job.services.job_post_demo.controller.JobPostController.getAllJobPosts())")
    public void logAfter(){
        System.out.println("After get posts method called Logging...");
    }

    // This will be logged only if successful execution completed (If exception thrown, it won't be logged)
    @AfterReturning("execution(public * com.job.services.job_post_demo.controller.JobPostController.getAllJobPosts())")
    public void logAfterRunning(){
        System.out.println("After running get posts method called Logging...");
    }

    // This will be logged only if execution thrown
    @AfterThrowing("execution(public * com.job.services.job_post_demo.controller.JobPostController.getAllJobPosts())")
    public void logAfterThrowing(){
        System.out.println("Exception thrown.....");
    }
}

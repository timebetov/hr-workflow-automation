package com.github.timebetov;

import com.github.timebetov.config.AppConfig;
import com.github.timebetov.model.Candidate;
import com.github.timebetov.model.Job;
import com.github.timebetov.model.User;
import com.github.timebetov.service.CandidateService;
import com.github.timebetov.service.JobService;
import com.github.timebetov.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        JobService jobService = context.getBean(JobService.class);

        List<Job> jobs = jobService.getAllJobs();
        System.out.println("-".repeat(150));
        System.out.println("All jobs: " + jobs.size());
        jobs.forEach(System.out::println);
        System.out.println("-".repeat(150));
    }
}

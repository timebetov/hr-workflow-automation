package com.hrworkflow.jobservice;

import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.service.JobService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Iterator;
import java.util.List;

@SpringBootApplication
public class JobServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobServiceApplication.class, args);

//		JobService jobService = ctx.getBean(JobService.class);

//		Job juniorJava = jobService.createJob(
//				"Junior Java Developer",
//				"Must know develop applications using Spring Boot",
//				"Development"
//		);
//
//		Job internJava = jobService.createJob(
//				"Intern Java Developer",
//				"Requires basic knowledge of Java",
//				"Development"
//		);

//		jobService.closeJob("JYRZf5UBBWXzTd3m7v1-");
//		System.out.println("Открытые вакансии: ");
//		jobService.getJobsByStatus(JobStatus.OPEN).forEach(System.out::println);
	}

}

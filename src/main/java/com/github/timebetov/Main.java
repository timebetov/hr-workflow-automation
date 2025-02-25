package com.github.timebetov;

import com.github.timebetov.config.AppConfig;
import com.github.timebetov.dto.Candidate;
import com.github.timebetov.dto.Interview;
import com.github.timebetov.dto.Job;
import com.github.timebetov.service.CandidateService;
import com.github.timebetov.service.InterviewService;
import com.github.timebetov.service.JobService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {

    public static void main(String[] args) {


        // Loading Spring Application context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        CandidateService candidateService = context.getBean(CandidateService.class);

        // List of mock candidates
        Candidate candidate1 = Candidate.builder().name("Tim").email("tim@gmail.com").build();
        Candidate candidate2 = Candidate.builder().name("Bob").email("bob@gmail.com").build();
        Candidate candidate3 = Candidate.builder().name("John").email("john@gmail.com").build();
        Candidate candidate4 = Candidate.builder().name("Mark").email("mark@gmail.com").build();
        Candidate candidate5 = Candidate.builder().name("Alice").email("alice@gmail.com").build();

        // Add candidates
        candidateService.addCandidate(candidate1);
        candidateService.addCandidate(candidate2);
        candidateService.addCandidate(candidate3);
        candidateService.addCandidate(candidate4);
        candidateService.addCandidate(candidate5);

        // Get all candidates
        List<Candidate> candidates = candidateService.getAllCandidates();
        System.out.println(">> List of candidates:");
        candidates.forEach(System.out::println);

        // Get candidate by id
        Candidate candidate = candidateService.getCandidate(3);
        System.out.println("Candiate with id 3: " + candidate);

        // Update candidate
        Candidate updateCandidate = Candidate.builder().name("Jane").email("jane@gmail.com").build();

        candidate = candidateService.updateCandidate(2, updateCandidate);
        System.out.println("Updated candidate: " + candidate);

        // Remove candidate
        candidateService.removeCandidate(3);
        System.out.println("After deleting candidate with id 3:");
        candidateService.getAllCandidates().forEach(System.out::println);

        System.out.println("-".repeat(50));

        // JOB TEST
        JobService jobService = context.getBean(JobService.class);

        // List of mock jobs
        Job job1 = Job.builder()
                .title("Java Developer")
                .description("Java developer position")
                .department("IT").build();
        Job job2 = Job.builder()
                .title("HR Manager")
                .description("Junior Human Resource position")
                .department("HR").build();
        Job job3 = Job.builder()
                .title("C++ Developer")
                .description("C++ developer position")
                .department("IT").build();

        // Add jobs
        jobService.addJob(job1);
        jobService.addJob(job2);
        jobService.addJob(job3);

        // Get all jobs
        List<Job> jobs = jobService.getAllJobs();
        System.out.println(">> List of jobs:");
        jobs.forEach(System.out::println);

        // Get job by id
        Job job = jobService.getJob(2);
        System.out.println("Job with id 2: " + job);

        // Close job
        jobService.closeJob(1);
        System.out.println("After closing job with id 1:");
        jobService.getAllJobs().forEach(System.out::println);

        // Adding candidates to job
        jobService.addCandidateToJob(1, candidate1);
        jobService.addCandidateToJob(1, candidate2);
        jobService.addCandidateToJob(2, candidate3);
        jobService.addCandidateToJob(2, candidate4);
        jobService.addCandidateToJob(3, candidate5);

        // Get jobs by status
        jobs = jobService.getJobsByStatus(Job.JobStatus.OPEN);
        System.out.println("Jobs with status OPEN:");
        jobs.forEach(System.out::println);


        // Remove job
        jobService.removeJob(3);
        System.out.println("After deleting job with id 3:");
        jobService.getAllJobs().forEach(System.out::println);

        System.out.println("-".repeat(50));


        // INTERVIEW TEST
        InterviewService interviewService = context.getBean(InterviewService.class);

        // List of mock interviews
        Interview interview1 = Interview.builder()
                .candidate(candidate1)
                .interviewer("Madd")
                .job(job1)
                .build();
        Interview interview2 = Interview.builder()
                .candidate(candidate2)
                .interviewer("Charly")
                .job(job2)
                .build();
        Interview interview3 = Interview.builder()
                .candidate(candidate3)
                .interviewer("Abdul")
                .job(job3)
                .build();


        // Add interviews
        interviewService.addInterview(interview1);
        interviewService.addInterview(interview2);
        interviewService.addInterview(interview3);

        // Get all interviews
        List<Interview> interviews = interviewService.getAllInterviews();
        System.out.println(">> List of interviews:");
        interviews.forEach(System.out::println);

        // Get interview by id
        Interview interview = interviewService.getInterviewById(2);
        System.out.println("Interview with id 2: " + interview);

        // Get interviews by status
        interviews = interviewService.getInterviewsByStatus(Interview.InterviewStatus.SCHEDULED);
        System.out.println("Interviews with status SCHEDULED:");
        interviews.forEach(System.out::println);

        // Changing interview status
        interviewService.setStatus(1, Interview.InterviewStatus.COMPLETED);
        System.out.println("After setting status for interview with id 1:");
        interviewService.getAllInterviews().forEach(System.out::println);

        // Remove interview
        interviewService.removeInterview(3);
        System.out.println("After deleting interview with id 3:");
        interviewService.getAllInterviews().forEach(System.out::println);

    }
}

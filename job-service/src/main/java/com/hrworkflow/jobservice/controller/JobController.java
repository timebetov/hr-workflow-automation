package com.hrworkflow.jobservice.controller;

import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public Job insertJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @GetMapping("/all")
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping
    public List<Job> getAllOpenJobs() {
        return jobService.getJobsByStatus(JobStatus.OPEN);
    }

    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobService.getJob(id);
    }

    @PutMapping("/{id}")
    public void updateJobStatus(@PathVariable Long id, @RequestParam String status) {
        jobService.updateJobStatus(id, JobStatus.valueOf(status.toUpperCase()));
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
    }

    // Get current job status
    @GetMapping("/checkStatus/{id}")
    public String checkStatus(@PathVariable Long id) {

        Job job = jobService.getJob(id);
        return job.getStatus().name();
    }

    // Comparing stream performance
    @GetMapping("/performance")
    public Map<String, Long> compareStreamPerformance() {
        return jobService.compareStreamPerformance();
    }

    // Get jobs by department and groups it by status
    @GetMapping("/jobs-by-department")
    public Map<String, Map<JobStatus, List<Job>>> getJobsByDepartment() {
        return jobService.getJobsByDepartment();
    }
}

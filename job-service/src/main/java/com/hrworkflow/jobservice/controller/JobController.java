package com.hrworkflow.jobservice.controller;

import com.hrworkflow.jobservice.dto.JobFilterRequestDTO;
import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public Job insertJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public Job getJobById(@PathVariable String id) {
        return jobService.getJob(id);
    }

    @PostMapping("/{id}")
    public void updateJobStatus(@PathVariable String id, @RequestParam String status) {
        jobService.updateJobStatus(id, JobStatus.valueOf(status.toUpperCase()));
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable String id) {
        jobService.deleteJob(id);
    }

    // Get current job status
    @GetMapping("/checkStatus/{id}")
    public String checkStatus(@PathVariable String id) {

        Job job = jobService.getJob(id);
        return job.getStatus().name();
    }

    // To get filtered data
    @PostMapping("/filter")
    public List<Job> filterJobs(@RequestBody JobFilterRequestDTO filterRequest) {

        return jobService.filterJobs(filterRequest);
    }

    // Get the Department with highest salary
    @GetMapping("/highest-salary")
    public Map.Entry<String, Double> getHighestSalary() {
        return jobService.getDepartmentWithHighestSalary();
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

    // Get all jobs by status and group them
    @GetMapping("/jobs-by-status")
    public Map<Boolean, List<Job>> getJobsByStatus() {
        return jobService.getJobsByStatus();
    }

    @GetMapping("/search")
    public List<Job> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary
    ) {

        return jobService.searchJobs(title, department, minSalary, maxSalary);
    }
}

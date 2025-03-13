package com.hrworkflow.jobservice.controller;

import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return StreamSupport.stream(jobService.getAllJobs().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/status/{status}")
    public List<Job> getJobsByStatus(
            @PathVariable(value = "status") String status) {
        return jobService.getJobsByStatus(JobStatus.valueOf(status));
    }

    @GetMapping("/{id}")
    public Job getJobById(@PathVariable String id) {
        return jobService.getJob(id);
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable String id) {
        jobService.deleteJob(id);
    }

    @PostMapping("/{id}")
    public void updateJobStatus(@PathVariable String id, @RequestParam String status) {
        jobService.updateJobStatus(id, JobStatus.valueOf(status.toUpperCase()));
    }

    @GetMapping("/checkStatus/{id}")
    public String checkStatus(@PathVariable String id) {

        Job job = jobService.getJob(id);
        return job.getStatus().name();
    }
}

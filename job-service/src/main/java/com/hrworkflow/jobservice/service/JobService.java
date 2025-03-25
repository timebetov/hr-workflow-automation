package com.hrworkflow.jobservice.service;

import com.hrworkflow.common.events.JobEvent;
import com.hrworkflow.common.exceptions.ResourceNotFoundException;
import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final KafkaTemplate<String, JobEvent> kafkaTemplate;

    public Job createJob(Job job) {

        Job savedJob = jobRepository.save(job);

        JobEvent newJob = JobEvent.builder()
                .id(savedJob.getId())
                .title(savedJob.getTitle())
                .description(savedJob.getDescription())
                .department(savedJob.getDepartment())
                .status(savedJob.getStatus().name())
                .deadline(savedJob.getDeadline())
                .salary(savedJob.getSalary())
                .jobType(savedJob.getJobType().name())
                .deadline(savedJob.getDeadline())
                .build();

        kafkaTemplate.send("job.created", newJob);

        return savedJob;
    }

    public List<Job> getAllJobs() {

        return jobRepository.findAll();
    }

    public List<Job> getJobsByStatus(JobStatus status) {
        return jobRepository.getJobsByStatus(status);
    }

    public Job getJob(Long jobId) {
        return jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job with id " + jobId + " not found")
        );
    }

    public void updateJobStatus(Long jobId, JobStatus status) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + jobId + " not found"));

        job.setStatus(status);
        jobRepository.save(job);
    }

    public void deleteJob(Long jobId) {

        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        }
        jobRepository.deleteById(jobId);
    }

    public Map<String, Long> compareStreamPerformance() {

        List<Job> jobs = getAllJobs();

        long startSequential = System.nanoTime();
        jobs.stream().filter(job -> job.getSalary() > 50000).toList();
        long durationSequential = System.nanoTime() - startSequential;

        long startParallel = System.nanoTime();
        jobs.parallelStream().filter(job -> job.getSalary() > 50000).toList();
        long durationParallel = System.nanoTime() - startParallel;

        Map<String, Long> performanceResult = new HashMap<>();
        performanceResult.put("sequential", durationSequential);
        performanceResult.put("parallel", durationParallel);

        return performanceResult;
    }

    public Map<String, Map<JobStatus, List<Job>>> getJobsByDepartment() {

        return getAllJobs().stream()
                .collect(Collectors.groupingBy(Job::getDepartment, Collectors.groupingBy(Job::getStatus)));
    }

}

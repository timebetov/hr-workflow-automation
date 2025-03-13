package com.hrworkflow.jobservice.service;

import com.hrworkflow.jobservice.dto.ResourceNotFoundException;
import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public Job createJob(Job job) {

        job.setStatus(JobStatus.OPEN);
        return jobRepository.save(job);
    }

    public Iterable<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getJobsByStatus(JobStatus jobStatus) {
        return jobRepository.findByStatus(jobStatus);
    }

    public Job getJob(String jobId) {
        return jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job with id " + jobId + " not found")
        );
    }

    public void updateJobStatus(String jobId, JobStatus status) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + jobId + " not found"));

        job.setStatus(status);
        jobRepository.save(job);
    }

    public void deleteJob(String jobId) {

        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        }
        jobRepository.deleteById(jobId);
    }
}

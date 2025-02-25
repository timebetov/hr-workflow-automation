package com.github.timebetov.service;

import com.github.timebetov.dto.Candidate;
import com.github.timebetov.dto.Job;
import com.github.timebetov.repository.JobRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepositoryInterface jobRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getJobsByStatus(Job.JobStatus status) {
        return jobRepository.findByStatus(status);
    }

    public Job getJob(int id) {
        return jobRepository.findById(id);
    }

    public boolean addJob(Job job) {

        Objects.requireNonNull(job, "Job cannot be null");
        return jobRepository.add(job);
    }

    public Job closeJob(int id) {
        return jobRepository.closeJob(id);
    }

    public boolean removeJob(int id) {
        return jobRepository.remove(id);
    }

    public Job addCandidateToJob(int jobId, Candidate candidate) {

        Objects.requireNonNull(candidate, "Candidate cannot be null");
        return jobRepository.addCandidateToJob(jobId, candidate);
    }
}

package com.github.timebetov.service;

import com.github.timebetov.model.Candidate;
import com.github.timebetov.model.Job;
import com.github.timebetov.model.User;
import com.github.timebetov.model.status.JobStatus;
import com.github.timebetov.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public boolean saveJob(Job job) {
        return jobRepository.save(job);
    }

    public Optional<Job> getJobById(UUID id) {
        return jobRepository.findById(id);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public boolean updateJob(UUID id, Job job) {
        return jobRepository.update(id, job);
    }

    public boolean deleteJob(UUID id) {
        return jobRepository.delete(id);
    }

    public boolean closeJob(UUID id) {
        return jobRepository.closeJob(id);
    }

    public List<Job> getJobsByStatus(JobStatus status) { return jobRepository.findByStatus(status); }

    public List<Candidate> getCandidates(UUID id) { return jobRepository.getCandidates(id); }
}

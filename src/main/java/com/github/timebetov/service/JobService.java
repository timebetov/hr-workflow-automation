package com.github.timebetov.service;

import com.github.timebetov.model.Job;
import com.github.timebetov.model.status.JobStatus;
import com.github.timebetov.repository.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    @Transactional
    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    @Transactional(readOnly = true)
    public Job getJobById(UUID id) {

        return jobRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Job with id " + id + " not found")
        );
    }

    @Transactional(readOnly = true)
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Transactional
    public void deleteJob(UUID id) {

        if (!jobRepository.existsById(id)) {
            throw new EntityNotFoundException("Job with id " + id + " not found");
        }
        jobRepository.deleteById(id);
    }

    @Transactional
    public boolean closeJob(UUID id) {

        if (!jobRepository.existsById(id)) {
            throw new EntityNotFoundException("Job with id " + id + " not found");
        }
        return jobRepository.closeJob(id) > 0;
    }

    @Transactional
    public List<Job> getJobsByStatus(JobStatus status) {
        return jobRepository.findByStatus(status);
    }
}

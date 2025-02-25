package com.github.timebetov.repository;

import com.github.timebetov.dto.Candidate;
import com.github.timebetov.dto.Job;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JobRepository implements JobRepositoryInterface {

    private final List<Job> jobs = new ArrayList<>();

    @Override
    public List<Job> findAll() {
        return new ArrayList<>(jobs);
    }

    @Override
    public List<Job> findByStatus(Job.JobStatus status) {
        return new ArrayList<>(jobs.stream()
                .filter(j -> j.getStatus() == status)
                .toList());
    }

    @Override
    public Job findById(int id) {
        return jobs.stream()
                .filter(j -> j.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean add(Job job) {

        if (findById(job.getId()) != null) {
            throw new IllegalArgumentException("Job with ID: " + job.getId() + " already exists");
        }

        return jobs.add(job);
    }

    @Override
    public Job closeJob(int id) {

        Job job = findById(id);
        if (null == job) {
            throw new IllegalArgumentException("Job with ID: " + id + " not found");
        }
        job.setStatus(Job.JobStatus.CLOSED);
        return job;
    }

    @Override
    public boolean remove(int id) {
        return jobs.removeIf(j -> j.getId() == id);
    }

    @Override
    public Job addCandidateToJob(int jobId, Candidate candidate) {

        Job job = findById(jobId);
        if (job == null) {
            throw new IllegalArgumentException("Job with ID: " + jobId + " not found");
        }
        job.getCandidates().forEach(c -> {
            if (c.getId() == candidate.getId()) {
                throw new IllegalArgumentException("Candidate with ID: " + candidate.getId() + " already exists");
            }
        });
        job.addCandidate(candidate);
        return job;
    }
}

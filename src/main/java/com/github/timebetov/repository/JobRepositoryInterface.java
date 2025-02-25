package com.github.timebetov.repository;

import com.github.timebetov.dto.Candidate;
import com.github.timebetov.dto.Job;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepositoryInterface {

    List<Job> findAll();
    List<Job> findByStatus(Job.JobStatus status);
    Job findById(int id);
    boolean add(Job job);
    Job closeJob(int id);
    boolean remove(int id);

    Job addCandidateToJob(int jobId, Candidate candidate);
}

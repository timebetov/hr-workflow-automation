package com.github.timebetov.repository;

import com.github.timebetov.model.Candidate;
import com.github.timebetov.model.Job;
import com.github.timebetov.model.User;
import com.github.timebetov.model.status.JobStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobRepository {

    boolean save(Job job);
    Optional<Job> findById(UUID id);
    List<Job> findAll();
    boolean update(UUID id, Job job);
    boolean delete(UUID id);

    boolean closeJob(UUID id);
    List<Job> findByStatus(JobStatus status);
    List<Candidate> getCandidates(UUID id);
}

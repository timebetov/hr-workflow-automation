package com.github.timebetov.repository;

import com.github.timebetov.model.Candidate;
import com.github.timebetov.model.Job;
import com.github.timebetov.model.status.CandidateStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CandidateRepository {

    boolean save(Candidate candidate);
    Optional<Candidate> findById(UUID id);
    List<Candidate> findAll();
    boolean setStatus(UUID id, CandidateStatus status);
    boolean delete(UUID id);

    boolean applyForJob(UUID candidateId, UUID jobId);
    List<Job> findAppliedJobs(UUID candidateId);
}

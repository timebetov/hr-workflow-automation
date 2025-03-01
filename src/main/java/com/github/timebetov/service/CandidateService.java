package com.github.timebetov.service;

import com.github.timebetov.model.Candidate;
import com.github.timebetov.model.Job;
import com.github.timebetov.model.User;
import com.github.timebetov.model.status.CandidateStatus;
import com.github.timebetov.repository.CandidateRepository;
import com.github.timebetov.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CandidateService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;

    @Autowired
    public CandidateService(UserRepository userRepository, CandidateRepository candidateRepository) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
    }

    @Transactional
    public boolean registerCandidate(Candidate candidate) {

        User savedUser = userRepository.save(candidate);
        if (savedUser == null) return false;
        candidate.setId(savedUser.getId());
        return candidateRepository.save(candidate);
    }

    public Optional<Candidate> getCandidateById(UUID id) {
        return candidateRepository.findById(id);
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public boolean setStatus(UUID id, CandidateStatus status) {

        return candidateRepository.setStatus(id, status);
    }

    @Transactional
    public boolean deleteCandidate(UUID id) {

        boolean isDeleted = candidateRepository.delete(id);
        if (!isDeleted) return false;
        return userRepository.delete(id);
    }

    public boolean applyForJob(UUID candidateId, UUID jobId) {

        boolean isApplied = candidateRepository.applyForJob(candidateId, jobId);
        if (!isApplied)
            throw new IllegalStateException("You Have already applied for this job!");

        return isApplied;
    }

    public List<Job> getAppliedJobs(UUID candidateId) {
        return candidateRepository.findAppliedJobs(candidateId);
    }
}

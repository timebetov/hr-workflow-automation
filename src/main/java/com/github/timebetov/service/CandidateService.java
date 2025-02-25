package com.github.timebetov.service;

import com.github.timebetov.dto.Candidate;
import com.github.timebetov.repository.CandidateRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepositoryInterface candidateRepository;

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidate(int id) {

        Candidate candidate = candidateRepository.findById(id);
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate with ID: " + id + " not found");
        }
        return candidate;
    }

    public boolean addCandidate(Candidate candidate) {

        Objects.requireNonNull(candidate, "Candidate cannot be null");
        return candidateRepository.add(candidate);
    }

    public Candidate updateCandidate(int id, Candidate candidate) {

        Objects.requireNonNull(candidate, "Candidate cannot be null");
        return candidateRepository.update(id, candidate);
    }

    public boolean removeCandidate(int id) {
        return candidateRepository.remove(id);
    }
}

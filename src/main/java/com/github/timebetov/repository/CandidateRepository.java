package com.github.timebetov.repository;

import com.github.timebetov.dto.Candidate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateRepository implements CandidateRepositoryInterface {

    private final List<Candidate> candidates = new ArrayList<>();

    @Override
    public List<Candidate> findAll() {
        return new ArrayList<>(candidates);
    }

    @Override
    public Candidate findById(int id) {

        return candidates.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean add(Candidate candidate) {

        if (null != findById(candidate.getId())) {
            throw new IllegalArgumentException("Candidate with ID: " + candidate.getId() + " already exists");
        }
        return candidates.add(candidate);
    }

    @Override
    public Candidate update(int id, Candidate candidate) {

        Candidate candidateToUpdate = findById(id);
        if (null == candidateToUpdate) {
            throw new IllegalArgumentException("Candidate with ID: " + candidate.getId() + " not found");
        }
        candidateToUpdate.setName(candidate.getName());
        candidateToUpdate.setEmail(candidate.getEmail());

        return candidateToUpdate;
    }

    @Override
    public boolean remove(int id) {
        return candidates.removeIf(c -> c.getId() == id);
    }
}
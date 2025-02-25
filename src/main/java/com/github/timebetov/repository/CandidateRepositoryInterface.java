package com.github.timebetov.repository;

import com.github.timebetov.dto.Candidate;

import java.util.List;

public interface CandidateRepositoryInterface {

    // Get all candidates
    List<Candidate> findAll();

    // Get one specific candidate by id
    Candidate findById(int id);

    // Add a new candidate
    boolean add(Candidate candidate);

    // Update a specific candidate
    Candidate update(int id, Candidate candidate);

    // Delete a specific candidate
    boolean remove(int id);
}

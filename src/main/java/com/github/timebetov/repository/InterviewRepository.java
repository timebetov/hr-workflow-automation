package com.github.timebetov.repository;

import com.github.timebetov.dto.Interview;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InterviewRepository implements InterviewRepositoryInterface {

    private final List<Interview> interviews = new ArrayList<>();

    @Override
    public List<Interview> findAll() {
        return new ArrayList<>(interviews);
    }

    @Override
    public Interview findById(int id) {
        return interviews.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Interview> findByStatus(Interview.InterviewStatus status) {

        return new ArrayList<>(interviews.stream()
                .filter(i -> i.getStatus() == status)
                .toList());
    }

    @Override
    public boolean add(Interview interview) {

        if (findById(interview.getId()) != null) {
            throw new IllegalArgumentException("Interview with ID: " + interview.getId() + " already exists");
        }

        return interviews.add(interview);
    }

    @Override
    public Interview setStatus(int id, Interview.InterviewStatus status) {

        Interview interview = findById(id);
        if (interview == null) {
            throw new IllegalArgumentException("Interview with ID: " + id + " not found");
        }
        interview.setStatus(status);
        return interview;
    }


    @Override
    public boolean remove(int id) {
        return interviews.removeIf(i -> i.getId() == id);
    }
}

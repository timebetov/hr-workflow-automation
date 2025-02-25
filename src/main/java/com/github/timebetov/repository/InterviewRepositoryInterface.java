package com.github.timebetov.repository;

import com.github.timebetov.dto.Interview;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepositoryInterface {

    List<Interview> findAll();
    Interview findById(int id);
    List<Interview> findByStatus(Interview.InterviewStatus status);
    boolean add(Interview interview);
    Interview setStatus(int id, Interview.InterviewStatus status);
    boolean remove(int id);
}

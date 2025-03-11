package com.hrworkflow.workflowservice.repository;

import com.hrworkflow.workflowservice.model.Interview;
import com.hrworkflow.workflowservice.model.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    Optional<Interview> findByApplicationId(Long applicationId);
    List<Interview> findByInterviewerId(Integer interviewerId);
    List<Interview> findByStatus(InterviewStatus status);
}

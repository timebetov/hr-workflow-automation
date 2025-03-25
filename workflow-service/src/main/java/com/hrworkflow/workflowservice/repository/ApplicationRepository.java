package com.hrworkflow.workflowservice.repository;

import com.hrworkflow.workflowservice.model.Application;
import com.hrworkflow.workflowservice.model.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByCandidateId(Long candidateId);
    List<Application> findByJobId(Long jobId);
    List<Application> findByStatus(ApplicationStatus status);

    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);
}

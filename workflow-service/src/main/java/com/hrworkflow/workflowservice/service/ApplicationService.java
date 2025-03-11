package com.hrworkflow.workflowservice.service;

import com.hrworkflow.workflowservice.model.Application;
import com.hrworkflow.workflowservice.model.ApplicationStatus;
import com.hrworkflow.workflowservice.repository.ApplicationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public Application findById(Long applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow(() ->
                new EntityNotFoundException("Application with id " + applicationId + " not found"));
    }

    public List<Application> findByCandidateId(Integer candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    public List<Application> findByJobId(String jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public List<Application> findByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status);
    }

    public Application updateStatus(Long applicationId, ApplicationStatus status) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application with id " + applicationId + " not found"));

        application.setStatus(status);
        return applicationRepository.save(application);
    }

    public void delete(Application application) {

        if (!applicationRepository.existsById(application.getId())) {
            throw new EntityNotFoundException("Application with id " + application.getId() + " not found");
        }
        applicationRepository.delete(application);
    }
}

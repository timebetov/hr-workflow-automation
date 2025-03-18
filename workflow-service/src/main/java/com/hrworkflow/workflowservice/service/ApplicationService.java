package com.hrworkflow.workflowservice.service;

import com.hrworkflow.workflowservice.dto.ApplyDTO;
import com.hrworkflow.workflowservice.dto.ResourceNotFoundException;
import com.hrworkflow.workflowservice.dto.SetStatusDTO;
import com.hrworkflow.workflowservice.feignclient.JobClient;
import com.hrworkflow.workflowservice.model.Application;
import com.hrworkflow.workflowservice.model.ApplicationStatus;
import com.hrworkflow.workflowservice.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobClient jobClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.topics.log-info}")
    private String infoLogTopic;

    // Creating a new application
    public Application createApplication(ApplyDTO applyDTO) {

        // Checking for null
        if (applyDTO.getJobId() == null) {
            throw new IllegalArgumentException("JobId cannot be null");
        }

        // Extracting details
        String jobId = applyDTO.getJobId();
        Long candidateId = applyDTO.getCandidateId();

        // TODO: TO CHECK USER ROLE FOR ADMIN OR HR

        String msg = null;

        // Checking if User already applied for job
        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, jobId)) {
            msg = String.format("User %d already applied to job %s", candidateId, jobId);
            throw new IllegalArgumentException(msg);
        }

        /*
         * Making REST call to Job Service sending JobId
         * And checking for job status
         * Is CLOSED or OPEN
         */
        String jobStatus = jobClient.checkStatus(jobId);
        if (jobStatus.equals("CLOSED")) {
            msg = String.format("Application rejected: Job %s is already closed", jobId);
            throw new IllegalArgumentException(msg);
        }

        // Creating application object to save
        Application application = Application.builder()
                .candidateId(candidateId)
                .jobId(jobId)
                .status(ApplicationStatus.PENDING)
                .appliedAt(LocalDateTime.now())
                .build();
        Application savedApp = applicationRepository.save(application);

        // Success case
        msg = String.format(
                "{" +
                        "\"applicationId\": %d, " +
                        "\"jobId\": \"%s\", " +
                        "\"candidateId\": %d, " +
                        "\"message\": \"%s\"" +
                "}",
                savedApp.getId(), applyDTO.getJobId(), applyDTO.getCandidateId(), "Applied for job");
        kafkaTemplate.send(infoLogTopic, msg);
        return application;
    }

    public Application updateStatus(Long applicationId, SetStatusDTO statusDTO) {

        // Extracting data from DTO
        ApplicationStatus newStatus = statusDTO.getNewStatus();
        Long userId = statusDTO.getUserId();

        /*
        * TODO: TO CHECK USER ROLE FOR ADMIN OR HR
        *
        * throw new EntityNotFoundException("Only HR or ADMIN can change application status. User ID: " + userId);
        */

        String msg = null;

        // Getting the application from DB
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application with id " + applicationId + " not found"));

        // Checking if possible to switch the application status
        ApplicationStatus currentStatus = application.getStatus();
        if (!isValidStatusTransition(currentStatus, statusDTO.getNewStatus())) {
            msg = "Invalid status transition: " + currentStatus + " -> " + statusDTO.getNewStatus();
            throw new IllegalArgumentException(msg);
        }

        // Changing the application status
        application.setStatus(statusDTO.getNewStatus());
        applicationRepository.save(application);

        // Logging
        String messageToEvent = String.format("{\"applicationId\": %d, \"newStatus\": \"%s\", \"updatedBy\": %d, \"message\": \"%s\"}",
                applicationId, statusDTO.getNewStatus().name(), statusDTO.getUserId(), "Changed the application status");
        kafkaTemplate.send(infoLogTopic, messageToEvent);
        return application;
    }

    private boolean isValidStatusTransition(ApplicationStatus currentStatus, ApplicationStatus newStatus) {

        return switch (currentStatus) {
            case PENDING -> List.of(ApplicationStatus.INVITED, ApplicationStatus.REJECTED, ApplicationStatus.WITHDRAWN).contains(newStatus);
            case INVITED -> List.of(ApplicationStatus.HIRED, ApplicationStatus.REJECTED, ApplicationStatus.DECLINED).contains(newStatus);
            default -> false;
        };
    }

    public void delete(Long applicationId) {

        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Application with id " + applicationId + " not found");
        }
        applicationRepository.deleteById(applicationId);
    }

    public Application findById(Long applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow(() ->
                new ResourceNotFoundException("Application with id " + applicationId + " not found"));
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public List<Application> findByCandidateId(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    public List<Application> findByJobId(String jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public List<Application> findByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status);
    }
}

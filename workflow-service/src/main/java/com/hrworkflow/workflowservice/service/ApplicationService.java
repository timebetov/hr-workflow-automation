package com.hrworkflow.workflowservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrworkflow.common.dto.MessageType;
import com.hrworkflow.common.exceptions.ResourceNotFoundException;
import com.hrworkflow.workflowservice.feignclient.JobClient;
import com.hrworkflow.workflowservice.model.Application;
import com.hrworkflow.workflowservice.model.ApplicationStatus;
import com.hrworkflow.workflowservice.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobClient jobClient;
    private final UserService userService;
    private final NotificationService notificationService;

    // Creating a new application
    public Application createApplication(Long jobId) throws JsonProcessingException {

        if (jobId <= 0) {
            throw new IllegalArgumentException("Please provide a valid job id");
        }

        Long candidateId = userService.getCurrentUser().getUserId();
        String msg = null;

        // Checking if User already applied for job
        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, jobId)) {
            msg = String.format("User %d already applied to job %d", candidateId, jobId);
            throw new IllegalArgumentException(msg);
        }

        /*
         * Making REST call to Job Service sending JobId
         * And checking for job status
         * Is CLOSED or OPEN
         */
        String jobStatus = jobClient.checkStatus(jobId);
        if (jobStatus.equals("CLOSED")) {
            msg = String.format("Application rejected: Job %d is already closed", jobId);
            throw new IllegalArgumentException(msg);
        }

        // Creating application object to save
        Application application = Application.builder()
                .candidateId(candidateId)
                .jobId(jobId)
                .status(ApplicationStatus.PENDING)
                .appliedAt(LocalDateTime.now())
                .build();
        applicationRepository.save(application);
        notificationService.sendNotification(candidateId, "You applied for job: " + jobId, MessageType.APPLICATION);
        return application;
    }

    public Application updateStatus(Long applicationId, String newStatusToSet) throws JsonProcessingException {

        // Extracting data from DTO
        ApplicationStatus newStatus = ApplicationStatus.valueOf(newStatusToSet);
        Long userId = userService.getCurrentUser().getUserId();

        // Getting the application from DB
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application with id " + applicationId + " not found"));

        // Checking if possible to switch the application status
        ApplicationStatus currentStatus = application.getStatus();
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            String msg = "Invalid status transition: " + currentStatus + " -> " + newStatus;
            throw new IllegalArgumentException(msg);
        }

        // Changing the application status
        application.setStatus(newStatus);
        applicationRepository.save(application);

        notificationService.sendNotification(application.getCandidateId(), "Status of your application #" + applicationId + " changed to " + newStatus, MessageType.APPLICATION);
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

    public List<Application> findByJobId(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public List<Application> findByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status);
    }

    public List<Application> findMyApplications() {

        Long candidateId = userService.getCurrentUser().getUserId();
        return applicationRepository.findByCandidateId(candidateId);
    }
}

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

    public Application createApplication(ApplyDTO applyDTO) {

        String jobId = applyDTO.getJobId();
        int candidateId = applyDTO.getCandidateId();

        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, jobId)) {
            log.warn("User {} already applied for job {}", candidateId, jobId);
            throw new IllegalArgumentException(String.format("User %s already applied for job %s", candidateId, jobId));
        }

        String jobStatus = jobClient.checkStatus(jobId);

        if (jobStatus.equals("CLOSED")) {
            log.warn("Application rejected: Job {} is CLOSED", jobId);
            throw new IllegalArgumentException(String.format("Application rejected: Job %s is CLOSED", jobId));
        }

        Application application = Application.builder()
                .candidateId(candidateId)
                .jobId(jobId)
                .status(ApplicationStatus.PENDING)
                .appliedAt(LocalDateTime.now())
                .build();


        applicationRepository.save(application);
        String msgToLogEvent = String.format("User %s applied for job %s", candidateId, jobId);
        kafkaTemplate.send(infoLogTopic, msgToLogEvent);
        return application;
    }

    public Application findById(Long applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow(() ->
                new ResourceNotFoundException("Application with id " + applicationId + " not found"));
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
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

    public Application updateStatus(Long applicationId, SetStatusDTO statusDTO) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application with id " + applicationId + " not found"));

        ApplicationStatus currentStatus = application.getStatus();
        if (!isValidStatusTransition(currentStatus, statusDTO.getNewStatus())) {

            String errorMsg = "Invalid status transition: " + currentStatus + " -> " + statusDTO.getNewStatus();
            throw new IllegalArgumentException(errorMsg);
        }

        application.setStatus(statusDTO.getNewStatus());
        applicationRepository.save(application);
        String msgToLog = "Changed the application status";
        String messageToEvent = String.format("{\"applicationId\": %d, \"status\": \"%s\", \"userId\": %d, \"message\": \"%s\"}",
                applicationId, statusDTO.getNewStatus().name(), statusDTO.getUserId(), msgToLog);

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
}

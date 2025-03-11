package com.hrworkflow.workflowservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrworkflow.workflowservice.model.Application;
import com.hrworkflow.workflowservice.model.ApplicationStatus;
import com.hrworkflow.workflowservice.repository.ApplicationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationEventListener {

    private final ApplicationRepository applicationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.topics.job-request}")
    private String getJobDetailsRequestTopic;

    @Value("${app.topics.application-new}")
    private String newApplicationTopic;

    @Value("${app.topics.log-info}")
    private String infoLogTopic;

    @KafkaListener(topics = "${app.topics.application-new}")
    public void listenNewApplicationEvents(String message) throws JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(message);
        Integer candidateId = jsonNode.get("candidateId").asInt();
        String jobId = jsonNode.get("jobId").asText();

        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, jobId)) {
            log.warn("User {} already applied for job {}", candidateId, jobId);
            return;
        }

        String jobStatusRequestMessage = "{\"jobId\":\"" + jobId + "\",\"candidateId\":" + candidateId + "}";
        kafkaTemplate.send(getJobDetailsRequestTopic, jobStatusRequestMessage);
    }

    @KafkaListener(topics = "${app.topics.job-response}")
    public void processJobResponse(String message) throws JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(message);
        String jobId = jsonNode.get("jobId").asText();
        String status = jsonNode.get("status").asText();
        Integer candidateId = jsonNode.get("candidateId").asInt();

        if ("CLOSED".equals(status)) {
            log.warn("Application rejected: Job {} is CLOSED", jobId);
            return;
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
    }

    @KafkaListener(topics = "${app.topics.application-upd}")
    public void processApplicationUpdate(String message) throws JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(message);
        Long applicationId = jsonNode.get("applicationId").asLong();
        ApplicationStatus newStatus = ApplicationStatus.valueOf(jsonNode.get("status").asText());
        Integer userId = jsonNode.get("userId").asInt();

        Application application = applicationRepository.findById(applicationId).orElseThrow(
                () -> new EntityNotFoundException("Application with given ID: " + applicationId + " not found")
        );

        ApplicationStatus currentStatus = application.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {

            String errorMsg = "Invalid status transition: " + currentStatus + " -> " + newStatus;
            throw new IllegalArgumentException(errorMsg);
        }

        application.setStatus(newStatus);
        applicationRepository.save(application);

        String msgToLog = "Changed the application status";
        String messageToEvent = String.format("{\"applicationId\": %d, \"status\": \"%s\", \"userId\": %d, \"message\": \"%s\"}",
                applicationId, newStatus.name(), userId, msgToLog);
        kafkaTemplate.send(infoLogTopic, messageToEvent);

        // In future add updated_by (userId)
    }

    private boolean isValidStatusTransition(ApplicationStatus currentStatus, ApplicationStatus newStatus) {

        return switch (currentStatus) {
            case PENDING -> List.of(ApplicationStatus.INVITED, ApplicationStatus.REJECTED, ApplicationStatus.WITHDRAWN).contains(newStatus);
            case INVITED -> List.of(ApplicationStatus.HIRED, ApplicationStatus.REJECTED, ApplicationStatus.DECLINED).contains(newStatus);
            default -> false;
        };
    }
}

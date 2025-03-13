package com.hrworkflow.usersservice.service;

import com.hrworkflow.usersservice.dto.ApplicationDTO;
import com.hrworkflow.usersservice.dto.ApplicationStatus;
import com.hrworkflow.usersservice.dto.ApplyDTO;
import com.hrworkflow.usersservice.dto.ResourceNotFoundException;
import com.hrworkflow.usersservice.feignclient.WorkflowClient;
import com.hrworkflow.usersservice.model.Role;
import com.hrworkflow.usersservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final UserRepository userRepository;
    private final WorkflowClient workflowClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.topics.application-new}")
    private String applicationNewTopic;

    @Value("${app.topics.application-upd}")
    private String applicationUpdTopic;


    // Applying for job (ONLY Candidate)
    public boolean applyForJob(ApplyDTO apply) {

        if (apply.getJobId() == null) {
            throw new IllegalArgumentException("JobId cannot be null");
        }

        if (!userRepository.existsByIdAndRoleIs(apply.getCandidateId(), Role.CANDIDATE)) {
            throw new ResourceNotFoundException("Candidate with given id: " + apply.getCandidateId() + " not found");
        }

        String msgToLog = "Applied for job";
        String messageToEvent = String.format("{\"jobId\": \"%s\", \"candidateId\": %d, \"message\": \"%s\"}",
                apply.getJobId(), apply.getCandidateId(), msgToLog);

        kafkaTemplate.send(applicationNewTopic, messageToEvent);

        return workflowClient.createApplication(apply);
    }

    // Changing application status (ONLY HR and ADMIN)
    public boolean reqToChangeApplicationStatus(ApplicationDTO appDTO) {

        int userId = appDTO.getUserId();
        ApplicationStatus status = appDTO.getStatus();
        Long applicationId = appDTO.getApplicationId();

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (!(userRepository.existsByIdAndRoleIs(userId, Role.HR) || userRepository.existsByIdAndRoleIs(userId, Role.ADMIN))) {
            throw new EntityNotFoundException("Only HR or ADMIN can change application status. User ID: " + userId);
        }

        String msgToLog = "Requested to change application status";
        String messageToEvent = String.format("{\"applicationId\": %d, \"status\": \"%s\", \"userId\": %d, \"message\": \"%s\"}",
                applicationId, status, userId, msgToLog);

        kafkaTemplate.send(applicationUpdTopic, messageToEvent);
        return workflowClient.updateApplicationStatus(appDTO.getApplicationId(), appDTO);
    }
}

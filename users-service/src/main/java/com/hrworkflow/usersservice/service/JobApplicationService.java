package com.hrworkflow.usersservice.service;

import com.hrworkflow.usersservice.dto.workflow.ApplicationStatus;
import com.hrworkflow.usersservice.dto.workflow.ApplyDTO;
import com.hrworkflow.usersservice.dto.ResourceNotFoundException;
import com.hrworkflow.usersservice.dto.workflow.SetStatusDTO;
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
    public boolean reqToChangeApplicationStatus(Long applicationId, SetStatusDTO statusDTO) {

        ApplicationStatus newStatus = statusDTO.getNewStatus();
        Long userId = statusDTO.getUserId();

        if (!(userRepository.existsByIdAndRoleIs(userId, Role.HR) || userRepository.existsByIdAndRoleIs(userId, Role.ADMIN))) {
            throw new EntityNotFoundException("Only HR or ADMIN can change application status. User ID: " + userId);
        }

        String msgToLog = "Requested to change application status";
        String messageToEvent = String.format("{\"applicationId\": %d, \"status\": \"%s\", \"userId\": %d, \"message\": \"%s\"}",
                applicationId, newStatus, userId, msgToLog);

        kafkaTemplate.send(applicationUpdTopic, messageToEvent);
        return workflowClient.updateApplicationStatus(applicationId, statusDTO);
    }
}

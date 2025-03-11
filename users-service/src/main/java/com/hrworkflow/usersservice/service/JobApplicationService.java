package com.hrworkflow.usersservice.service;

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
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.topics.application-new}")
    private String applicationNewTopic;

    @Value("${app.topics.application-upd}")
    private String applicationUpdTopic;


    // Applying for job (ONLY Candidate)
    public void applyForJob(Integer candidateId, String jobId) {

        if (jobId == null) {
            throw new IllegalArgumentException("JobId cannot be null");
        }

        if (!userRepository.existsByIdAndRoleIs(candidateId, Role.CANDIDATE)) {
            throw new EntityNotFoundException("Candidate with given id: " + candidateId + " not found");
        }

        String msgToLog = "Applied for job";
        String messageToEvent = String.format("{\"jobId\": \"%s\", \"candidateId\": %d, \"message\": \"%s\"}",
                jobId, candidateId, msgToLog);

        kafkaTemplate.send(applicationNewTopic, messageToEvent);
    }

    // Changing application status (ONLY HR and ADMIN)
    public void reqToChangeApplicationStatus(Long applicationId, Integer userId, String status) {

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
    }
}

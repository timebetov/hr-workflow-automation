package com.hrworkflow.workflowservice.service;

import com.hrworkflow.workflowservice.dto.ResourceNotFoundException;
import com.hrworkflow.workflowservice.model.Interview;
import com.hrworkflow.workflowservice.model.InterviewStatus;
import com.hrworkflow.workflowservice.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.topics.interview}")
    private String interviewTopic;

    public Interview scheduleInterview(Long applicationId, Integer interviewerId, LocalDateTime interviewDate) {

        Interview interview = Interview.builder()
                .applicationId(applicationId)
                .interviewerId(interviewerId)
                .interviewDate(interviewDate)
                .status(InterviewStatus.SCHEDULED)
                .build();

        Interview savedInterview = interviewRepository.save(interview);

        kafkaTemplate.send(interviewTopic, "Interview scheduled for application: " + applicationId);
        return savedInterview;
    }

    public List<Interview> findAll() {
        return interviewRepository.findAll();
    }

    public Interview findById(Long id) {

        return interviewRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Interview not found with id: " + id));
    }

    public Interview findByApplicationId(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId).orElseThrow(
                () -> new ResourceNotFoundException("Interview not found for applicationId: " + applicationId)
        );
    }

    public List<Interview> findByInterviewerId(Integer interviewerId) {
        return interviewRepository.findByInterviewerId(interviewerId);
    }

    public List<Interview> findByStatus(InterviewStatus status) {
        return interviewRepository.findByStatus(status);
    }

    public Interview udpateStatus(Long interviewId, InterviewStatus newStatus) {

        Interview interview = interviewRepository.findById(interviewId).orElseThrow(
                () -> new ResourceNotFoundException("Interview with given ID: " + interviewId + " not found")
        );
        interview.setStatus(newStatus);
        return interviewRepository.save(interview);
    }

    public Interview updateDate(Long interviewId, LocalDateTime newDate) {

        Interview interview = interviewRepository.findById(interviewId).orElseThrow(
                () -> new ResourceNotFoundException("Interview with given ID: " + interviewId + " not found"));
        interview.setInterviewDate(newDate);
        Interview savedInterview = interviewRepository.save(interview);

        String kafkaMessage = "Interview with id: " + interviewId + " rescheduled to: " + newDate;
        kafkaTemplate.send(interviewTopic, kafkaMessage);
        return savedInterview;
    }

    public void delete(Long interviewId) {

        if (!interviewRepository.existsById(interviewId)) {
            throw new ResourceNotFoundException("Interview with given ID: " + interviewId + " does not exist");
        }
        interviewRepository.deleteById(interviewId);
    }

}

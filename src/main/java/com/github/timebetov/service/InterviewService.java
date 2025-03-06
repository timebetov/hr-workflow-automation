package com.github.timebetov.service;

import com.github.timebetov.model.Application;
import com.github.timebetov.model.Interview;
import com.github.timebetov.model.User;
import com.github.timebetov.model.status.ApplicationStatus;
import com.github.timebetov.model.status.InterviewStatus;
import com.github.timebetov.repository.ApplicationRepository;
import com.github.timebetov.repository.InterviewRepository;
import com.github.timebetov.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Interview scheduleInterview(UUID applicationId, UUID interviewerId, LocalDateTime scheduledAt, String notes) {

        if (scheduledAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled date must be in the future");
        }

        Application application = applicationRepository.findById(applicationId).orElseThrow(
                () -> new EntityNotFoundException("Application with id " + applicationId + " not found")
        );
        if (application.getInterview() != null) {
            throw new IllegalStateException("Application with id " + applicationId + " already has an interview scheduled");
        }

        application.setStatus(ApplicationStatus.INVITED);

        User interviewer = userRepository.findById(interviewerId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + interviewerId + " not found")
        );

        Interview interview = Interview.builder()
                .application(application)
                .interviewer(interviewer)
                .scheduledAt(scheduledAt)
                .status(InterviewStatus.SCHEDULED)
                .notes(notes)
                .build();

        return interviewRepository.save(interview);
    }

    @Transactional
    public void updateInterviewStatus(UUID interviewId, InterviewStatus status, String notes, ApplicationStatus applicationStatus) {

        Interview interview = interviewRepository.findById(interviewId).orElseThrow(
                () -> new EntityNotFoundException("Interview with id " + interviewId + " not found")
        );
        Application currentApplication = interview.getApplication();
        currentApplication.setStatus(applicationStatus);

        int updatedRows = interviewRepository.updateInterviewStatus(interviewId, status, notes);
        if (updatedRows == 0) {
            throw new EntityNotFoundException("Interview with id " + interviewId + " not found");
        }
    }

    @Transactional(readOnly = true)
    public List<Interview> findInterviewsByApplication(UUID applicationId) {
        return interviewRepository.findByApplicationId(applicationId);
    }

    @Transactional(readOnly = true)
    public List<Interview> findInterviewsByInterviewer(UUID interviewerId) {
        return interviewRepository.findByInterviewerId(interviewerId);
    }
}

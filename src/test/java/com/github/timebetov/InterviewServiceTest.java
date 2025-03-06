package com.github.timebetov;

import com.github.timebetov.model.Application;
import com.github.timebetov.model.Interview;
import com.github.timebetov.model.User;
import com.github.timebetov.model.status.ApplicationStatus;
import com.github.timebetov.model.status.InterviewStatus;
import com.github.timebetov.repository.ApplicationRepository;
import com.github.timebetov.repository.InterviewRepository;
import com.github.timebetov.repository.UserRepository;
import com.github.timebetov.service.InterviewService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InterviewService interviewService;

    @Test
    void shouldScheduleInterview() {
        UUID appId = UUID.randomUUID();
        UUID interviewerId = UUID.randomUUID();
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        Application application = new Application();
        User interviewer = new User();
        Interview interview = Interview.builder()
                .application(application)
                .interviewer(interviewer)
                .scheduledAt(futureDate)
                .status(InterviewStatus.SCHEDULED)
                .notes("Initial interview")
                .build();

        when(applicationRepository.findById(appId)).thenReturn(Optional.of(application));
        when(userRepository.findById(interviewerId)).thenReturn(Optional.of(interviewer));
        when(interviewRepository.save(any(Interview.class))).thenReturn(interview);

        Interview result = interviewService.scheduleInterview(appId, interviewerId, futureDate, "Initial interview");

        assertNotNull(result);
        assertEquals(InterviewStatus.SCHEDULED, result.getStatus());
        verify(interviewRepository).save(any(Interview.class));
    }

    @Test
    void shouldThrowWhenSchedulingPastInterview() {
        UUID appId = UUID.randomUUID();
        UUID interviewerId = UUID.randomUUID();
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
                interviewService.scheduleInterview(appId, interviewerId, pastDate, "Invalid"));
    }

    @Test
    void shouldThrowWhenApplicationNotFound() {
        UUID appId = UUID.randomUUID();
        UUID interviewerId = UUID.randomUUID();
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        when(applicationRepository.findById(appId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                interviewService.scheduleInterview(appId, interviewerId, futureDate, "Invalid"));
    }

    @Test
    void shouldThrowWhenInterviewerNotFound() {
        UUID appId = UUID.randomUUID();
        UUID interviewerId = UUID.randomUUID();
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        when(applicationRepository.findById(appId)).thenReturn(Optional.of(new Application()));
        when(userRepository.findById(interviewerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                interviewService.scheduleInterview(appId, interviewerId, futureDate, "Invalid"));
    }

    @Test
    void shouldThrowWhenApplicationAlreadyHasInterview() {
        UUID appId = UUID.randomUUID();
        UUID interviewerId = UUID.randomUUID();
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        Application application = new Application();
        application.setInterview(new Interview());

        when(applicationRepository.findById(appId)).thenReturn(Optional.of(application));

        assertThrows(IllegalStateException.class, () ->
                interviewService.scheduleInterview(appId, interviewerId, futureDate, "Invalid"));
    }

    @Test
    void shouldUpdateInterviewStatus() {
        UUID interviewId = UUID.randomUUID();
        Interview interview = new Interview();
        Application application = new Application();
        interview.setApplication(application);

        when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(interview));
        when(interviewRepository.updateInterviewStatus(interviewId, InterviewStatus.COMPLETED, "Done"))
                .thenReturn(1);

        assertDoesNotThrow(() -> interviewService.updateInterviewStatus(
                interviewId, InterviewStatus.COMPLETED, "Done", ApplicationStatus.HIRED));
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentInterview() {
        UUID interviewId = UUID.randomUUID();

        when(interviewRepository.findById(interviewId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                interviewService.updateInterviewStatus(interviewId, InterviewStatus.COMPLETED, "Done", ApplicationStatus.HIRED));
    }

    @Test
    void shouldFindInterviewsByApplication() {
        UUID appId = UUID.randomUUID();
        List<Interview> interviews = List.of(new Interview(), new Interview());

        when(interviewRepository.findByApplicationId(appId)).thenReturn(interviews);

        List<Interview> result = interviewService.findInterviewsByApplication(appId);
        assertEquals(2, result.size());
    }

    @Test
    void shouldFindInterviewsByInterviewer() {
        UUID interviewerId = UUID.randomUUID();
        List<Interview> interviews = List.of(new Interview(), new Interview());

        when(interviewRepository.findByInterviewerId(interviewerId)).thenReturn(interviews);

        List<Interview> result = interviewService.findInterviewsByInterviewer(interviewerId);
        assertEquals(2, result.size());
    }
}

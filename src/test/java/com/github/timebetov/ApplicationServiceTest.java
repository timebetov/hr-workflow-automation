package com.github.timebetov;

import com.github.timebetov.model.Application;
import com.github.timebetov.model.Job;
import com.github.timebetov.model.User;
import com.github.timebetov.model.status.ApplicationStatus;
import com.github.timebetov.repository.ApplicationRepository;
import com.github.timebetov.repository.JobRepository;
import com.github.timebetov.repository.UserRepository;
import com.github.timebetov.service.ApplicationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private UUID candidateId;
    private UUID jobId;
    private UUID applicationId;
    private User candidate;
    private Job job;
    private Application application;

    @BeforeEach
    void setUp() {
        candidateId = UUID.randomUUID();
        jobId = UUID.randomUUID();
        applicationId = UUID.randomUUID();
        candidate = new User();
        job = new Job();
        application = Application.builder()
                .id(applicationId)
                .candidate(candidate)
                .job(job)
                .status(ApplicationStatus.PENDING)
                .build();
    }

    @Test
    void shouldGetAllApplications() {
        when(applicationRepository.findAll()).thenReturn(List.of(application));
        List<Application> result = applicationService.getAllApplications();
        assertEquals(1, result.size());
        verify(applicationRepository).findAll();
    }

    @Test
    void shouldApplyForJobSuccessfully() {
        when(userRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        applicationService.applyForJob(candidateId, jobId);

        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    void shouldThrowExceptionWhenCandidateNotFound() {
        when(userRepository.findById(candidateId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> applicationService.applyForJob(candidateId, jobId));
    }

    @Test
    void shouldThrowExceptionWhenJobNotFound() {
        when(userRepository.findById(candidateId)).thenReturn(Optional.of(candidate));
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> applicationService.applyForJob(candidateId, jobId));
    }

    @Test
    void shouldUpdateApplicationStatusSuccessfully() {

        when(applicationRepository.updateApplicationStatus(applicationId, ApplicationStatus.HIRED)).thenReturn(1);

        boolean updated = applicationService.updateApplicationStatus(applicationId, ApplicationStatus.HIRED);

        assertTrue(updated);
        verify(applicationRepository, times(1)).updateApplicationStatus(applicationId, ApplicationStatus.HIRED);
    }

    @Test
    void shouldReturnFalseWhenUpdatingNonexistentApplication() {
        when(applicationRepository.updateApplicationStatus(applicationId, ApplicationStatus.PENDING)).thenReturn(0);

        boolean updated = applicationService.updateApplicationStatus(applicationId, ApplicationStatus.PENDING);

        assertFalse(updated);
    }

    @Test
    void shouldGetApplicationsByCandidate() {
        when(applicationRepository.findByCandidateId(candidateId)).thenReturn(List.of(application));

        List<Application> result = applicationService.getApplicationsByCandidate(candidateId);

        assertEquals(1, result.size());
        verify(applicationRepository).findByCandidateId(candidateId);
    }

    @Test
    void shouldGetApplicationsByJob() {
        when(applicationRepository.findByJobId(jobId)).thenReturn(List.of(application));

        List<Application> result = applicationService.getApplicationByJob(jobId);

        assertEquals(1, result.size());
        verify(applicationRepository).findByJobId(jobId);
    }
}

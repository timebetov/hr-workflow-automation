package com.github.timebetov;

import com.github.timebetov.model.Job;
import com.github.timebetov.model.status.JobStatus;
import com.github.timebetov.repository.JobRepository;
import com.github.timebetov.service.JobService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private Job mockJob;
    private UUID jobId;

    @BeforeEach
    void setUp() {

        jobId = UUID.randomUUID();
        mockJob = Job.builder()
                .id(jobId)
                .title("Test Job")
                .description("Test description")
                .status(JobStatus.ACTIVE)
                .build();
    }

    @Test
    void saveJob_ShouldSaveAndReturnJob() {

        when(jobRepository.save(any(Job.class))).thenReturn(mockJob);

        Job result = jobService.saveJob(mockJob);

        assertNotNull(result);
        assertEquals(jobId, result.getId());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void shouldGetJobById() {
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(mockJob));

        Job foundJob = jobService.getJobById(jobId);

        assertThat(foundJob).isEqualTo(mockJob);
        verify(jobRepository, times(1)).findById(jobId);
    }

    @Test
    void shouldThrowExceptionWhenJobNotFound() {
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobService.getJobById(jobId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Job with id " + jobId + " not found");

        verify(jobRepository, times(1)).findById(jobId);
    }

    @Test
    void shouldGetAllJobs() {
        List<Job> jobs = List.of(mockJob);
        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> result = jobService.getAllJobs();

        assertThat(result).hasSize(1).contains(mockJob);
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteJob() {
        when(jobRepository.existsById(jobId)).thenReturn(true);

        jobService.deleteJob(jobId);

        verify(jobRepository, times(1)).deleteById(jobId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentJob() {
        when(jobRepository.existsById(jobId)).thenReturn(false);

        assertThatThrownBy(() -> jobService.deleteJob(jobId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Job with id " + jobId + " not found");

        verify(jobRepository, never()).deleteById(jobId);
    }

    @Test
    void shouldCloseJob() {
        when(jobRepository.existsById(jobId)).thenReturn(true);
        when(jobRepository.closeJob(jobId)).thenReturn(1);

        boolean isClosed = jobService.closeJob(jobId);

        assertThat(isClosed).isTrue();
        verify(jobRepository, times(1)).closeJob(jobId);
    }

    @Test
    void shouldGetJobsByStatus() {
        List<Job> jobs = List.of(mockJob);
        when(jobRepository.findByStatus(JobStatus.ACTIVE)).thenReturn(jobs);

        List<Job> result = jobService.getJobsByStatus(JobStatus.ACTIVE);

        assertThat(result).hasSize(1).contains(mockJob);
        verify(jobRepository, times(1)).findByStatus(JobStatus.ACTIVE);
    }
}

package com.hrworkflow.jobservice;

import com.hrworkflow.jobservice.dto.ResourceNotFoundException;
import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.model.JobType;
import com.hrworkflow.jobservice.repository.JobRepository;
import com.hrworkflow.jobservice.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobCrudServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private Job job;

    @BeforeEach
    void setUp() {

        job = Job.builder()
                .id("d7a35587-1976-42ce-ba46-bfb73bd2e876")
                .title("Java Developer")
                .department("IT")
                .status(JobStatus.OPEN)
                .salary(100000.0)
                .postedAt(LocalDate.now())
                .deadline(LocalDate.now().plusDays(7))
                .jobType(JobType.FULL_TIME)
                .build();
    }

    @Test
    void shouldCreateJob() {

        // When the save method is called with any Job object
        // it should return the predefined job instance
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        // Create a new job using the service
        Job createdJob = jobService.createJob(job);

        // Ensure that the created job is not null
        assertNotNull(createdJob);

        // Verify that the job title matches the expected value
        assertEquals("Java Developer", createdJob.getTitle());
        // Verify that the job status is set to OPEN
        assertEquals(JobStatus.OPEN, createdJob.getStatus());

        // Ensure that the save method of jobRepository is called exactly once
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void shouldReturnAllJobs() {

        when(jobRepository.findAll()).thenReturn(List.of(job));
        List<Job> allJobs = jobService.getAllJobs();
        assertFalse(allJobs.isEmpty());
        assertEquals(1, allJobs.size());
    }

    @Test
    void shouldReturnJobById() {

        String id = "d7a35587-1976-42ce-ba46-bfb73bd2e876";
        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        Job job = jobService.getJob(id);
        assertNotNull(job);
        assertEquals("d7a35587-1976-42ce-ba46-bfb73bd2e876", job.getId());
        verify(jobRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowExceptionIfJobNotFound() {

        when(jobRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> jobService.getJob(anyString()));
    }

    @Test
    void shouldUpdateJobStatus() {

        when(jobRepository.findById(anyString())).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        jobService.updateJobStatus(job.getId(), JobStatus.CLOSED);

        assertEquals(JobStatus.CLOSED, job.getStatus());
        verify(jobRepository, times(1)).save(job);
    }

    @Test
    void shouldDeleteJob() {

        when(jobRepository.existsById(anyString())).thenReturn(true);
        doNothing().when(jobRepository).deleteById(anyString());

        jobService.deleteJob(job.getId());

        verify(jobRepository, times(1)).deleteById(anyString());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentJob() {

        when(jobRepository.existsById("2")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> jobService.deleteJob("2"));
    }
}

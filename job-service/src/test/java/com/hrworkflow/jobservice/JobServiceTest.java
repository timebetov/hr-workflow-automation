package com.hrworkflow.jobservice;

import com.hrworkflow.common.events.JobEvent;
import com.hrworkflow.common.exceptions.ResourceNotFoundException;
import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.model.JobType;
import com.hrworkflow.jobservice.repository.JobRepository;
import com.hrworkflow.jobservice.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private KafkaTemplate<String, JobEvent> kafkaTemplate;

    @InjectMocks
    private JobService jobService;

    private Job job;

    @BeforeEach
    void setUp() {

        job = Job.builder()
                .id(1L)
                .title("Java Developer")
                .department("IT")
                .status(JobStatus.OPEN)
                .salary(100000.0)
                .deadline(LocalDateTime.now().plusDays(7))
                .jobType(JobType.FULL_TIME)
                .build();
    }

    @Test
    void shouldCreateJobAndSendKafkaEvent() {

        when(jobRepository.save(any(Job.class))).thenReturn(job);

        Job createdJob = jobService.createJob(job);

        assertNotNull(createdJob);
        assertEquals("Java Developer", createdJob.getTitle());
        assertEquals(JobStatus.OPEN, createdJob.getStatus());

        verify(jobRepository, times(1)).save(any(Job.class));

        ArgumentCaptor<JobEvent> jobEventCaptor = ArgumentCaptor.forClass(JobEvent.class);
        verify(kafkaTemplate, times(1)).send(eq("job.created"), jobEventCaptor.capture());

        JobEvent sentEvent = jobEventCaptor.getValue();
        assertNotNull(sentEvent);
        assertEquals(job.getId(), sentEvent.getId());
        assertEquals(job.getTitle(), sentEvent.getTitle());
        assertEquals(job.getDepartment(), sentEvent.getDepartment());
        assertEquals(job.getSalary(), sentEvent.getSalary());
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

        Long id = 1L;
        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        Job job = jobService.getJob(id);
        assertNotNull(job);
        assertEquals(1L, job.getId());
        verify(jobRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowExceptionIfJobNotFound() {

        when(jobRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> jobService.getJob(anyLong()));
    }

    @Test
    void shouldUpdateJobStatus() {

        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        jobService.updateJobStatus(job.getId(), JobStatus.CLOSED);

        assertEquals(JobStatus.CLOSED, job.getStatus());
        verify(jobRepository, times(1)).save(job);
    }

    @Test
    void shouldDeleteJob() {

        when(jobRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(jobRepository).deleteById(anyLong());

        jobService.deleteJob(job.getId());

        verify(jobRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentJob() {

        when(jobRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> jobService.deleteJob(2L));
    }
}

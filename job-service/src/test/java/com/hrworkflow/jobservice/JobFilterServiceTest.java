package com.hrworkflow.jobservice;

import com.hrworkflow.jobservice.dto.JobFilterRequestDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobFilterServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private List<Job> jobs = new ArrayList<>();

    @BeforeEach
    void setUp() {

        jobs.add(Job.builder()
                .id("d7a35587-1976-42ce-ba46-bfb73bd2e876")
                .title("Junior Java Developer")
                .department("IT")
                .status(JobStatus.OPEN)
                .salary(400000.0)
                .postedAt(LocalDate.now())
                .deadline(LocalDate.now().plusDays(7))
                .jobType(JobType.FULL_TIME)
                .build());
        jobs.add(Job.builder()
                .id("d7a35587-1876-42ce-ba46-bfb73bd2e776")
                .title("Senior C++ Developer")
                .department("IT")
                .status(JobStatus.CLOSED)
                .salary(1500000.0)
                .postedAt(LocalDate.now())
                .deadline(LocalDate.now().plusDays(7))
                .jobType(JobType.FULL_TIME)
                .build());
        jobs.add(Job.builder()
                .id("d7a35587-1276-42ce-ba46-bfb73bd2e176")
                .title("Junior HR Manager")
                .department("HR")
                .status(JobStatus.OPEN)
                .salary(300000.0)
                .postedAt(LocalDate.now())
                .deadline(LocalDate.now().plusDays(7))
                .jobType(JobType.FULL_TIME)
                .build());
        jobs.add(Job.builder()
                .id("d7a35587-1276-42ce-ba46-bfb73bd2e176")
                .title("Middle HR Manager")
                .department("HR")
                .status(JobStatus.CLOSED)
                .salary(400000.0)
                .postedAt(LocalDate.now())
                .deadline(LocalDate.now().plusDays(7))
                .jobType(JobType.FULL_TIME)
                .build());
    }

    @Test
    void shouldPartitionJobsByStatus() {
        when(jobRepository.findAll()).thenReturn(jobs);

        Map<Boolean, List<Job>> partitionedJobs = jobService.getJobsByStatus();

        assertEquals(2, partitionedJobs.get(true).size());
        assertEquals(2, partitionedJobs.get(false).size());
    }

    @Test
    void shouldGetDepartmentWithHighestSalary() {

        when(jobRepository.findAll()).thenReturn(jobs);

        Map.Entry<String, Double> departmentWithHighestSalary = jobService.getDepartmentWithHighestSalary();

        assertEquals("IT", departmentWithHighestSalary.getKey());
        assertEquals(Double.valueOf(1500000.0), departmentWithHighestSalary.getValue());
    }

    @Test
    void shouldReturnFilteredJobs() {

        JobFilterRequestDTO filter = JobFilterRequestDTO.builder()
                .department("IT")
                .maxSalary(600000.0)
                .build();

        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> filteredJobs = jobService.filterJobs(filter);

        assertEquals(1, filteredJobs.size());
        assertEquals("IT", filteredJobs.get(0).getDepartment());
        assertEquals(Double.valueOf(400000.0), filteredJobs.get(0).getSalary());
    }

    @Test
    void shouldCompareStreamPerformance() {
        when(jobRepository.findAll()).thenReturn(jobs);

        Map<String, Long> result = jobService.compareStreamPerformance();

        assertTrue(result.containsKey("sequential"));
        assertTrue(result.containsKey("parallel"));
    }
}

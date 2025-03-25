package com.hrworkflow.searchservice.service;

import com.hrworkflow.common.events.JobEvent;
import com.hrworkflow.common.exceptions.ResourceNotFoundException;
import com.hrworkflow.searchservice.dto.JobDocument;
import com.hrworkflow.searchservice.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobListenerService {

    private final JobRepository jobRepository;

    @KafkaListener(topics = "job.created", groupId = "search-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleJobCreated(JobEvent event) throws IOException {

        JobDocument job = JobDocument.builder()
                .id(event.getId().toString())
                .title(event.getTitle())
                .description(event.getDescription())
                .department(event.getDepartment())
                .status(event.getStatus())
                .salary(event.getSalary())
                .jobType(event.getJobType())
                .deadline(event.getDeadline())
                .build();

        jobRepository.save(job);
        log.info("Job indexed in Elasticsearch: {}", job);
    }

    @KafkaListener(topics = "job.updated", groupId = "search-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleJobUpdated(JobEvent event) throws IOException {

        JobDocument job = jobRepository.findById(event.getId().toString()).orElseThrow(
                () -> new ResourceNotFoundException("Job not indexed")
        );

        job.setTitle(event.getTitle());
        job.setDescription(event.getDescription());
        job.setDepartment(event.getDepartment());
        job.setStatus(event.getStatus());
        job.setSalary(event.getSalary());
        job.setJobType(event.getJobType());
        job.setDeadline(event.getDeadline());
        jobRepository.save(job);

        log.info("Job: {} is updated", job);
    }

    @KafkaListener(topics = "job.deleted", groupId = "search-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleJobDeleted(JobEvent event) throws IOException {

        JobDocument job = jobRepository.findById(event.getId().toString()).orElseThrow(
                () -> new ResourceNotFoundException("Job not indexed")
        );
        jobRepository.deleteById(event.getId().toString());
        log.info("Job: {} is deleted", job);
    }

    @KafkaListener(topics = "job.status.changed", groupId = "search-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleJobStatusChanged(JobEvent event) throws IOException {

        JobDocument job = jobRepository.findById(event.getId().toString()).orElseThrow(
                () -> new ResourceNotFoundException("Job not indexed")
        );
        job.setStatus(event.getStatus());
        jobRepository.save(job);

        log.info("Job: {} status changed to {}", job, event.getStatus());
    }

    @KafkaListener(topics = "job.expired", groupId = "search-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleJobExpired(JobEvent event) throws IOException {

        JobDocument job = jobRepository.findById(event.getId().toString()).orElseThrow(
                () -> new ResourceNotFoundException("Job not indexed")
        );
        job.setDeadline(event.getDeadline());
        job.setStatus(event.getStatus());
        jobRepository.save(job);

        log.info("Job: {} expired", job);
    }
}

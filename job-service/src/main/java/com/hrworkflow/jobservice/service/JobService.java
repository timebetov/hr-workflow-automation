package com.hrworkflow.jobservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.topics.job-response}")
    private String jobResponseTopic;

    @Value("${app.topics.log-info}")
    private String logInfoTopic;

    @Value("${app.topics.log-error}")
    private String errorLogTopic;

    @KafkaListener(topics = "${app.topics.job-request}")
    public void fetchJobDetails(String jobStatusRequestMessage) {

        try {
            JsonNode jsonNode = objectMapper.readTree(jobStatusRequestMessage);
            String jobId = jsonNode.get("jobId").asText();
            Integer candidateId = jsonNode.get("candidateId").asInt();

            Job job = jobRepository.findById(jobId).orElse(null);

            String status = (job != null) ? job.getStatus().name() : "UNKNOWN";
            String msg = "Response to fetch job details";
            String responseMessage = String.format("{\"jobId\": \"%s\", \"status\": \"%s\", \"candidateId\": %d, \"message\", \"%s\"}", jobId, status, candidateId, msg);

            kafkaTemplate.send(jobResponseTopic, responseMessage);
            log.info(responseMessage);
        } catch (JsonProcessingException e) {

            String errorMsg = String.format("{\"reason\": \"%s\", \"processing\": \"%s\"}", e.getMessage(), jobStatusRequestMessage);
            kafkaTemplate.send(errorLogTopic, errorMsg);
            log.error(errorMsg);
        }


    }

    public Job createJob(String title, String description, String department) {

        Job job = Job.builder()
                .title(title)
                .description(description)
                .department(department)
                .status(JobStatus.OPEN)
                .build();
        return jobRepository.save(job);
    }

    public Iterable<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getJobsByStatus(JobStatus jobStatus) {
        return jobRepository.findByStatus(jobStatus);
    }

    public Job getJob(String jobId) {
        return jobRepository.findById(jobId).orElseThrow(RuntimeException::new);
    }

    public boolean closeJob(String jobId) {

        boolean jobIsClosed = false;
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            job.setStatus(JobStatus.CLOSED);
            jobRepository.save(job);
            jobIsClosed = true;
        }
        return jobIsClosed;
    }

    public void deleteJob(String jobId) {

        if (!jobRepository.existsById(jobId)) {
            throw new RuntimeException("Job not found with id: " + jobId);
        }
        jobRepository.deleteById(jobId);
    }
}

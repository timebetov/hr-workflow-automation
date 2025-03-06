package com.github.timebetov.service;

import com.github.timebetov.model.Application;
import com.github.timebetov.model.Job;
import com.github.timebetov.model.User;
import com.github.timebetov.model.status.ApplicationStatus;
import com.github.timebetov.repository.ApplicationRepository;
import com.github.timebetov.repository.JobRepository;
import com.github.timebetov.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Transactional
    public void applyForJob(UUID candidateId, UUID jobId) {

        User candidate = userRepository.findById(candidateId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + candidateId + " not found")
        );

        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new EntityNotFoundException("Job with id " + jobId + " not found")
        );

        Application application = Application.builder()
                .candidate(candidate)
                .job(job)
                .status(ApplicationStatus.PENDING)
                .build();

        applicationRepository.save(application);
    }

    @Transactional
    public boolean updateApplicationStatus(UUID applicationId, ApplicationStatus status) {
        return applicationRepository.updateApplicationStatus(applicationId, status) > 0;
    }

    @Transactional
    public List<Application> getApplicationsByCandidate(UUID candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    @Transactional
    public List<Application> getApplicationByJob(UUID jobId) {
        return applicationRepository.findByJobId(jobId);
    }
}

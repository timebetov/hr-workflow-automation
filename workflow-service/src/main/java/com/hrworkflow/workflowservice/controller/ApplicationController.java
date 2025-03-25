package com.hrworkflow.workflowservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrworkflow.workflowservice.model.Application;
import com.hrworkflow.workflowservice.model.ApplicationStatus;
import com.hrworkflow.workflowservice.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply/{jobId}")
    public boolean createApplication(@PathVariable Long jobId) throws JsonProcessingException {
        return applicationService.createApplication(jobId) != null;
    }

    @PatchMapping("/{id}")
    boolean updateApplicationStatus(
            @PathVariable("id") Long id,
            @RequestParam(value = "newStatus") String newStatus) throws JsonProcessingException {

        return applicationService.updateStatus(id, newStatus) != null;
    }

    @GetMapping("/{id}")
    public Application getApplication(@PathVariable Long id) {
        return applicationService.findById(id);
    }

    @GetMapping
    public List<Application> getAllApplications(
            @RequestParam(value = "candidateId", required = false) Long candidateId,
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "status", required = false) String status
    ) {

        List<Application> applications;

        if (candidateId != null) {
            applications = applicationService.findByCandidateId(candidateId);
        } else if (jobId != null) {
            applications = applicationService.findByJobId(jobId);
        } else if (status != null) {
            applications = applicationService.findByStatus(ApplicationStatus.valueOf(status));
        } else {
            applications = applicationService.findAll();
        }

        return applications;
    }

    @GetMapping("/myApplications")
    public List<Application> getMyApplications() {

        return applicationService.findMyApplications();
    }

    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable Long id) {
        applicationService.delete(id);
    }
}

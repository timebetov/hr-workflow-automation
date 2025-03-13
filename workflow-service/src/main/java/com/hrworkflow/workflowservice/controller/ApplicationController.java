package com.hrworkflow.workflowservice.controller;

import com.hrworkflow.workflowservice.dto.ApplicationDTO;
import com.hrworkflow.workflowservice.dto.ApplyDTO;
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

    @PostMapping
    public boolean createApplication(@RequestBody ApplyDTO applyDTO) {

        return applicationService.createApplication(applyDTO) != null;
    }

    @PatchMapping("/{id}")
    public boolean updateApplicationStatus(@PathVariable Long id, @RequestBody ApplicationDTO appDto) {

        appDto.setApplicationId(id);
        return applicationService.updateStatus(appDto) != null;
    }

    @GetMapping("/{id}")
    public Application getApplication(@PathVariable Long id) {
        return applicationService.findById(id);
    }

    @GetMapping
    public List<Application> getAllApplications(
            @RequestParam(value = "candidateId", required = false) int candidateId,
            @RequestParam(value = "jobId", required = false) String jobId,
            @RequestParam(value = "status", required = false) String status
    ) {

        List<Application> applications;

        if (candidateId >= 0) {
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

    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable Long id) {
        applicationService.delete(id);
    }
}

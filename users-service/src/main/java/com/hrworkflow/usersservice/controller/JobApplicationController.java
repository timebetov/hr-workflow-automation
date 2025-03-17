package com.hrworkflow.usersservice.controller;

import com.hrworkflow.usersservice.dto.workflow.ApplyDTO;
import com.hrworkflow.usersservice.dto.workflow.SetStatusDTO;
import com.hrworkflow.usersservice.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applies")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping("/apply")
    public boolean applyForJob(@RequestBody ApplyDTO applyDTO) {
        return jobApplicationService.applyForJob(applyDTO);
    }

    @PatchMapping("/{appid}")
    public boolean updateApplicationStatus(@PathVariable Long appid, @RequestBody SetStatusDTO statusDTO) {

        return jobApplicationService.reqToChangeApplicationStatus(appid, statusDTO);
    }
}

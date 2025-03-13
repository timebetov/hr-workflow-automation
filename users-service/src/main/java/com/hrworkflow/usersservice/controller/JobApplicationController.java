package com.hrworkflow.usersservice.controller;

import com.hrworkflow.usersservice.dto.ApplicationDTO;
import com.hrworkflow.usersservice.dto.ApplyDTO;
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

    @PatchMapping("")
    public boolean updateApplicationStatus(
            @PathVariable Integer appid, @RequestBody ApplicationDTO applyDTO) {

        return jobApplicationService.reqToChangeApplicationStatus(applyDTO);
    }
}

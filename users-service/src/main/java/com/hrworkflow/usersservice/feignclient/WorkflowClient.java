package com.hrworkflow.usersservice.feignclient;

import com.hrworkflow.usersservice.dto.workflow.ApplyDTO;
import com.hrworkflow.usersservice.dto.workflow.SetStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "WORKFLOW-SERVICE", path = "/workflow-app/api")
public interface WorkflowClient {

    @PostMapping("/applications")
    boolean createApplication(@RequestBody ApplyDTO applyDTO);

    @PatchMapping("/applications/{id}")
    boolean updateApplicationStatus(@PathVariable("id") Long id, @RequestBody SetStatusDTO statusDTO);
}

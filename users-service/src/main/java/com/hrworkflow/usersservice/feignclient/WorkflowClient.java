package com.hrworkflow.usersservice.feignclient;

import com.hrworkflow.usersservice.dto.ApplicationDTO;
import com.hrworkflow.usersservice.dto.ApplyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "WORKFLOW-SERVICE", path = "/workflow-app/api")
public interface WorkflowClient {

    @PostMapping
    boolean createApplication(@RequestBody ApplyDTO applyDTO);

    @PatchMapping("/{id}")
    boolean updateApplicationStatus(@PathVariable Long id, @RequestBody ApplicationDTO appDto);
}

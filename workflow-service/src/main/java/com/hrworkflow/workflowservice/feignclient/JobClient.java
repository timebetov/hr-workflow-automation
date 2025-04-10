package com.hrworkflow.workflowservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "JOB-SERVICE", path = "/jobs-app/api/jobs/")
public interface JobClient {

    @GetMapping("/checkStatus/{id}")
    String checkStatus(@PathVariable Long id);
}

package com.hrworkflow.workflowservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplyDTO {

    private String jobId;
    private int candidateId;
}

package com.hrworkflow.workflowservice.dto;

import com.hrworkflow.workflowservice.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationDTO {

    private Long applicationId;
    private ApplicationStatus status;
    private String jobId;
    private int userId;
    private LocalDateTime appliedAt;

}

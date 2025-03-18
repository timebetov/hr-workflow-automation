package com.hrworkflow.workflowservice.dto;

import com.hrworkflow.workflowservice.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SetStatusDTO {

    Long userId;
    ApplicationStatus newStatus;
}

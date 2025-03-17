package com.hrworkflow.usersservice.dto.workflow;

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

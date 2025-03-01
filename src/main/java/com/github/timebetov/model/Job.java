package com.github.timebetov.model;

import com.github.timebetov.model.status.JobStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
@SuperBuilder
public class Job extends BaseEntity {

    private UUID id;
    private String title;
    private String description;
    private JobStatus status;
}

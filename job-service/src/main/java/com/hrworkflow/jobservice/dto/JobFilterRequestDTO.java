package com.hrworkflow.jobservice.dto;

import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.model.JobType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobFilterRequestDTO {

    private String title;
    private String department;
    private JobStatus status;
    private Double minSalary;
    private Double maxSalary;
    private JobType jobType;
    private LocalDate postedAfter;
    private LocalDate postedBefore;
}

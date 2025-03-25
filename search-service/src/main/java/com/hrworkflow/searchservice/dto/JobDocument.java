package com.hrworkflow.searchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDocument {

    private String id;
    private String title;
    private String description;
    private String department;
    private String jobType;
    private String status;

    private LocalDateTime deadline;
    private Double salary;
}

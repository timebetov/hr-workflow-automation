package com.hrworkflow.searchservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobFilterRequestDTO {

    private String title;
    private String[] keywords;
    private String department;
    private Double minSalary;
    private Double maxSalary;
    private String jobType;
}

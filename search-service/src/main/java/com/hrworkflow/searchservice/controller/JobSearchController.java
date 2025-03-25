package com.hrworkflow.searchservice.controller;

import com.hrworkflow.searchservice.dto.JobDocument;
import com.hrworkflow.searchservice.dto.JobFilterRequestDTO;
import com.hrworkflow.searchservice.service.JobSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobSearchController {

    private final JobSearchService jobSearchService;

    @PostMapping
    public List<JobDocument> getFilteredData(@RequestBody JobFilterRequestDTO filterReq) throws IOException {

        return jobSearchService.searchJobs(filterReq);
    }

    @GetMapping
    public List<JobDocument> getFilteredJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            @RequestParam(required = false) String jobType) throws IOException {

        JobFilterRequestDTO filterReq = JobFilterRequestDTO.builder()
                .title(title)
                .department(department)
                .jobType(jobType)
                .minSalary(minSalary)
                .maxSalary(maxSalary)
                .keywords(keywords.toArray(new String[0]))
                .build();
        return jobSearchService.searchJobs(filterReq);
    }

    @GetMapping("/highest-salary")
    public Map.Entry<String, Double> getHighestSalary() throws IOException {
        return jobSearchService.getDepartmentWithHighestSalary();
    }
}

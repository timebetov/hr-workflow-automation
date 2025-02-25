package com.github.timebetov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(builderMethodName = "jobBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    private static int lastId = 0;

    public enum JobStatus {
        OPEN, CLOSED
    }

    private int id;
    private String title;
    private String description;
    private String department;
    private JobStatus status;

    /*
     * One-To-Many relationship
     * One job can have multiple candidates
     */
    private List<Candidate> candidates;

    public Job(String title, String description, String department) {
        this.id = ++lastId;
        this.title = title;
        this.description = description;
        this.department = department;
        this.status = JobStatus.OPEN;
    }

    public static JobBuilder builder() {
        return jobBuilder().id(++lastId).status(JobStatus.OPEN).candidates(new ArrayList<>());
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }
}

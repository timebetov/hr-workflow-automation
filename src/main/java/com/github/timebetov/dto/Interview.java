package com.github.timebetov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(builderMethodName = "interviewBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Interview {

    private static int lastId = 0;

    public enum InterviewStatus {
        SCHEDULED, CANCELLED, COMPLETED
    }

    private int id;
    private Candidate candidate;
    private Job job;
    private String interviewer;
    private InterviewStatus status;

    public Interview(Candidate candidate, Job job, String interviewer) {

        this.id = ++lastId;
        this.candidate = candidate;
        this.job = job;
        this.interviewer = interviewer;
        this.status = InterviewStatus.SCHEDULED;
    }

    public static InterviewBuilder builder() {
        return interviewBuilder().id(++lastId).status(InterviewStatus.SCHEDULED);
    }

}

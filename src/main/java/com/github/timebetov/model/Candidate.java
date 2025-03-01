package com.github.timebetov.model;

import com.github.timebetov.model.status.CandidateStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
@SuperBuilder
public class Candidate extends User {
    private CandidateStatus status;
    private UUID jobId;
}

package com.github.timebetov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "candidateBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    private static int lastId = 0;

    private int id;
    private String name;
    private String email;

    public Candidate(String name, String email) {
        this.id = ++lastId;
        this.name = name;
        this.email = email;
    }

    public static CandidateBuilder builder() {
        return candidateBuilder().id(++lastId);
    }
}

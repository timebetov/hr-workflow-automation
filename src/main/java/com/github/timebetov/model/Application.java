package com.github.timebetov.model;

import com.github.timebetov.model.status.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applications")
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private User candidate;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Job job;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Interview interview;
}

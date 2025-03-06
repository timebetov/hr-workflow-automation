package com.github.timebetov.model;

import com.github.timebetov.model.status.InterviewStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interviews")
public class Interview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id", nullable = false, unique = true)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "interviewer_id", referencedColumnName = "id", nullable = false)
    private User interviewer;

    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    private String notes;
}

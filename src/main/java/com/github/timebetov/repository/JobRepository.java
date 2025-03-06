package com.github.timebetov.repository;

import com.github.timebetov.model.Job;
import com.github.timebetov.model.status.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    List<Job> findByStatus(JobStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Job j SET j.status = 'CLOSED' WHERE j.id = :id")
    int closeJob(UUID id);
}

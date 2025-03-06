package com.github.timebetov.repository;

import com.github.timebetov.model.Application;
import com.github.timebetov.model.status.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    @Modifying
    @Query("UPDATE Application a SET a.status = :status WHERE a.id = :applicationId")
    int updateApplicationStatus(@Param("applicationId") UUID applicationId,
                                    @Param("status") ApplicationStatus status);

    List<Application> findByCandidateId(UUID candidateId);
    List<Application> findByJobId(UUID jobId);
}

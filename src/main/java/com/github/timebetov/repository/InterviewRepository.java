package com.github.timebetov.repository;

import com.github.timebetov.model.Interview;
import com.github.timebetov.model.status.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE Interview i SET i.status = :status, i.notes = :notes WHERE i.application.id = :applicationId")
    int updateInterviewStatus(@Param("applicationId") UUID applicationId,
                              @Param("status") InterviewStatus status,
                              @Param("notes") String notes);

    List<Interview> findByApplicationId(UUID applicationId);
    List<Interview> findByInterviewerId(UUID interviewerId);
}

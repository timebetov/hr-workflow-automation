package com.github.timebetov.repository;

import com.github.timebetov.model.User;
import com.github.timebetov.model.status.RoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    List<User> findByRole(RoleStatus role);
    List<User> findByPosition(String position);

    @Query("SELECT u FROM User u JOIN FETCH u.applications a WHERE a.job.id = :jobId")
    List<User> findByJobId(@Param("jobId") UUID jobId);
}

package com.github.timebetov.repository;

import com.github.timebetov.model.Candidate;
import com.github.timebetov.model.Job;
import com.github.timebetov.model.status.CandidateStatus;
import com.github.timebetov.rowMappers.CandidateRowMapper;
import com.github.timebetov.rowMappers.JobRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CandidateRepositoryImpl implements CandidateRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String DEF_SAVE_CANDIDATE = """
            INSERT INTO candidates 
            (id, status, created_by)
            VALUES (?, ?, ?)
            """;

    private static final String DEF_READ_CANDIDATE = """
            SELECT * FROM users u
            JOIN candidates c ON u.id = c.id
            WHERE u.id = ?
            """;
    private static final String DEF_READ_CANDIDATES = """
            SELECT * FROM users u
            INNER JOIN candidates c ON u.id = c.id
            """;

    private static final String DEF_SET_CANDIDATE_STATUS = """
            UPDATE candidates
            SET status = ?, updated_by = ?
            WHERE id = ?
            """;

    private static final String DEF_DELETE_CANDIDATE = "DELETE FROM candidates WHERE id = ?";

    private static final String DEF_APPLY_JOB = """
            INSERT INTO applications
            (id, candidate_id, job_id)
            SELECT ?, ?, ?
            FROM jobs
            WHERE id = ? AND status = 'ACTIVE'
            AND NOT EXISTS (
                      SELECT 1 FROM applications WHERE candidate_id = ? AND job_id = ?
                  )
            """;

    private static final String DEF_READ_CANDIDATE_JOBS = """
            SELECT j.*
            FROM jobs j
            JOIN applications a ON j.id = a.job_id
            WHERE a.candidate_id = ?
            """;


    @Autowired
    public CandidateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(Candidate candidate) {
        UUID candidateId = candidate.getId() != null ? candidate.getId() : UUID.randomUUID();
        return jdbcTemplate.update(DEF_SAVE_CANDIDATE,
                candidateId,
                CandidateStatus.JOB_SEEKING.name(),
                "ANONYMOUS") > 0;
    }

    @Override
    public Optional<Candidate> findById(UUID id) {
        return jdbcTemplate.query(DEF_READ_CANDIDATE, new CandidateRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Candidate> findAll() {
        return jdbcTemplate.query(DEF_READ_CANDIDATES, new CandidateRowMapper());
    }

    @Override
    public boolean setStatus(UUID id, CandidateStatus status) {
        return jdbcTemplate.update(DEF_SET_CANDIDATE_STATUS, status.name(), "SYSTEM", id) > 0;
    }

    @Override
    public boolean delete(UUID id) {
        return jdbcTemplate.update(DEF_DELETE_CANDIDATE, id) > 0;
    }

    @Override
    public boolean applyForJob(UUID candidateId, UUID jobId) {
        UUID applicationId = UUID.randomUUID();
        return jdbcTemplate.update(DEF_APPLY_JOB,
                applicationId, candidateId, jobId, jobId, candidateId, jobId) > 0;
    }

    @Override
    public List<Job> findAppliedJobs(UUID candidateId) {
        return jdbcTemplate.query(DEF_READ_CANDIDATE_JOBS, new JobRowMapper(), candidateId);
    }
}
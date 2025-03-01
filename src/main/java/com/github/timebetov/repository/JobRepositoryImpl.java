package com.github.timebetov.repository;

import com.github.timebetov.model.Candidate;
import com.github.timebetov.model.Job;
import com.github.timebetov.model.User;
import com.github.timebetov.model.status.JobStatus;
import com.github.timebetov.rowMappers.CandidateRowMapper;
import com.github.timebetov.rowMappers.JobRowMapper;
import com.github.timebetov.rowMappers.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JobRepositoryImpl implements JobRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String DEF_SAVE_JOB = """
            INSERT INTO jobs 
            (id, title, description, status, created_by) 
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String DEF_READ_JOBS = "SELECT * FROM jobs";
    private static final String DEF_READ_JOB = "SELECT * FROM jobs WHERE id = ?";

    private static final String DEF_UPDATE_JOB = """
            UPDATE jobs
            SET title = ?, description = ?, status = ?, updated_by = ?
            WHERE id = ?
            """;

    private static final String DEF_DELETE_JOB = "DELETE FROM jobs WHERE id = ?";
    private static final String DEF_CLOSE_JOB = """
            UPDATE jobs
            SET status = ?
            WHERE id = ?
            """;

    private static final String DEF_READ_JOBS_BY_STATUS = "SELECT * FROM jobs WHERE status = ?";

    private static final String DEF_READ_CANDIDATES = """
            SELECT u.*
            FROM users u
            INNER JOIN candidates c ON u.id = c.id
            INNER JOIN applications a ON c.id = a.candidate_id
            WHERE c.job_id = ?
            """;

    @Autowired
    public JobRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(Job job) {
        return jdbcTemplate.update(DEF_SAVE_JOB,
                UUID.randomUUID(),
                job.getTitle(),
                job.getDescription(),
                job.getStatus().name(),
                "ANONYMOUS") > 0;
    }

    @Override
    public Optional<Job> findById(UUID id) {
        return jdbcTemplate.query(DEF_READ_JOB, new JobRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Job> findAll() {
        return jdbcTemplate.query(DEF_READ_JOBS, new JobRowMapper());
    }

    @Override
    public boolean update(UUID id, Job job) {
        return jdbcTemplate.update(DEF_UPDATE_JOB,
                job.getTitle(),
                job.getDescription(),
                job.getStatus().name(),
                "ADMIN",
                id) > 0;
    }

    @Override
    public boolean delete(UUID id) {
        return jdbcTemplate.update(DEF_DELETE_JOB, id) > 0;
    }

    @Override
    public boolean closeJob(UUID id) {
        return jdbcTemplate.update(DEF_CLOSE_JOB,
                JobStatus.CLOSED.name(),
                id) > 0;
    }

    @Override
    public List<Job> findByStatus(JobStatus status) {
        return jdbcTemplate.query(DEF_READ_JOBS_BY_STATUS, new JobRowMapper(), status.name());
    }

    @Override
    public List<Candidate> getCandidates(UUID id) {
        return jdbcTemplate.query(DEF_READ_CANDIDATES, new CandidateRowMapper(), id);
    }
}

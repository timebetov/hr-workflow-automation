package com.github.timebetov.rowMappers;

import com.github.timebetov.model.Job;
import com.github.timebetov.model.status.JobStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class JobRowMapper implements RowMapper<Job> {

    @Override
    public Job mapRow(ResultSet rs, int rowNum) throws SQLException {
        Job job = Job.builder()
                .id(rs.getObject("id", UUID.class))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .status(JobStatus.valueOf(rs.getString("status")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .createdBy(rs.getString("created_by"))
                .updatedAt(rs.getTimestamp("updated_at") != null
                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                        : null)
                .updatedBy(rs.getString("updated_by") != null
                        ? rs.getString("updated_by")
                        : null)
                .build();

        return job;
    }
}

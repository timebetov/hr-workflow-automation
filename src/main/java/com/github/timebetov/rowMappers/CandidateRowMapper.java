package com.github.timebetov.rowMappers;

import com.github.timebetov.model.Candidate;
import com.github.timebetov.model.status.CandidateStatus;
import com.github.timebetov.model.status.RoleStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CandidateRowMapper implements RowMapper<Candidate> {

    @Override
    public Candidate mapRow(ResultSet rs, int rowNum) throws SQLException {

        Candidate candidate = Candidate.builder()
                .id(rs.getObject("id", UUID.class))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .role(RoleStatus.valueOf(rs.getString("role")))
                .status(CandidateStatus.valueOf(rs.getString("status")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .createdBy(rs.getString("created_by"))
                .updatedAt(rs.getTimestamp("updated_at") != null
                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                        : null)
                .updatedBy(rs.getString("updated_by") != null
                        ? rs.getString("updated_by")
                        : null)
                .build();

        return candidate;
    }
}

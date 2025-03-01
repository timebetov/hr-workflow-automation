package com.github.timebetov.repository;

import com.github.timebetov.model.User;
import com.github.timebetov.rowMappers.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String DEF_SAVE_USER = """
            INSERT INTO users 
            (id, first_name, last_name, email, password, role, created_by) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String DEF_READ_USER = """
            SELECT * FROM users
            WHERE id = ?
            """;

    private static final String DEF_READ_USERS = "SELECT * FROM users";
    private static final String DEF_UPDATE_USER = """
            UPDATE users
            SET first_name = ?, last_name = ?, email = ?, password = ?, role = ?, updated_by = ?
            WHERE id = ?
            """;

    private static final String DEF_DELETE_USER = "DELETE FROM users WHERE id = ?";

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        boolean rowsAffected = jdbcTemplate.update(DEF_SAVE_USER,
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().name(),
                "ANONYMOUS") > 0;

        if (rowsAffected) {
            return user;
        } else {
            throw new IllegalStateException("Failed to save user");
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jdbcTemplate.query(DEF_READ_USER, new UserRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(DEF_READ_USERS, new UserRowMapper());
    }

    @Override
    public boolean update(UUID id, User user) {
        return jdbcTemplate.update(DEF_UPDATE_USER,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().name(),
                "ANONYMOUS", id) > 0;
    }

    @Override
    public boolean delete(UUID id) {
        return jdbcTemplate.update(DEF_DELETE_USER, id) > 0;
    }
}

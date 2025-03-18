package com.hrworkflow.identityservice.repository;

import com.hrworkflow.identityservice.model.Role;
import com.hrworkflow.identityservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);

    boolean existsByIdAndRoleIs(Long userId, Role role);
}

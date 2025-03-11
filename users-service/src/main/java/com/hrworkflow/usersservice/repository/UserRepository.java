package com.hrworkflow.usersservice.repository;

import com.hrworkflow.usersservice.model.Role;
import com.hrworkflow.usersservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByPosition(String position);

    boolean existsByIdAndRoleIs(Integer candidateId, Role role);
}

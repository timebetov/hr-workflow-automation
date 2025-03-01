package com.github.timebetov.repository;

import com.github.timebetov.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);
    List<User> findAll();

    boolean update(UUID id, User user);
    boolean delete(UUID id);
}

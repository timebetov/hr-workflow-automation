package com.hrworkflow.identityservice.repository;

import com.hrworkflow.identityservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByUserIdOrderByCreatedAtDesc(Long userId);
}

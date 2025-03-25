package com.hrworkflow.workflowservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrworkflow.common.dto.MessageType;
import com.hrworkflow.common.events.MessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendNotification(Long userId, String message, MessageType type) throws JsonProcessingException {
        MessageEvent event = new MessageEvent(userId, message, type);
        String json = objectMapper.writeValueAsString(event);
        kafkaTemplate.send("notifications.events", json);
    }
}
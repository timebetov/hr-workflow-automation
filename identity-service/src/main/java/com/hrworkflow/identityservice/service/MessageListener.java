package com.hrworkflow.identityservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrworkflow.common.events.MessageEvent;
import com.hrworkflow.identityservice.model.Message;
import com.hrworkflow.identityservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListener {

    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "notifications.events")
    public void listenNotifications(String messageJson) throws JsonProcessingException {

        log.info("Received a new message: {}", messageJson);
        MessageEvent event = objectMapper.readValue(messageJson, MessageEvent.class);

        Message message = Message.builder()
                .userId(event.getUserId())
                .message(event.getMessage())
                .type(event.getType())
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);
        log.info("Message saved: {}", message);
    }
}

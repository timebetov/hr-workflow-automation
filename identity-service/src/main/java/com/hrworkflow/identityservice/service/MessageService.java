package com.hrworkflow.identityservice.service;

import com.hrworkflow.identityservice.dto.MessageDTO;
import com.hrworkflow.identityservice.model.Message;
import com.hrworkflow.identityservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    public List<MessageDTO> getMessagesForUser(Long userId) {

        List<Message> messages = messageRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return messages.stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }
}

package com.hrworkflow.identityservice.controller;

import com.hrworkflow.identityservice.dto.MessageDTO;
import com.hrworkflow.identityservice.service.MessageService;
import com.hrworkflow.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/{userId}")
    public List<MessageDTO> getMessages(@PathVariable Long userId) {

        return messageService.getMessagesForUser(userId);
    }

    @GetMapping("/myMessages")
    public List<MessageDTO> getMyMessages() {

        Long userId = userService.getCurrentUser().getUserId();
        return messageService.getMessagesForUser(userId);
    }
}

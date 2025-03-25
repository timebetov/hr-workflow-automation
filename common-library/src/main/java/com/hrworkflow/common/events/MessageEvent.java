package com.hrworkflow.common.events;

import com.hrworkflow.common.dto.MessageType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEvent {
    private Long userId;
    private String message;
    private MessageType type;
}

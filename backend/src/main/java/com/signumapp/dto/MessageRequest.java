package com.signumapp.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    Long conversationId;
    Long senderId;
    Long receiverId;
    String message;
    LocalDateTime timestamp;
}

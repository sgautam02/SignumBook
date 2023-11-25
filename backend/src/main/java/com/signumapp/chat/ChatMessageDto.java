package com.signumapp.chat;

import java.util.Date;

import lombok.Data;

@Data
public class ChatMessageDto {

    private String senderId;
    private String recipientId;
    private String content;
    private Date timestamp;
    
}

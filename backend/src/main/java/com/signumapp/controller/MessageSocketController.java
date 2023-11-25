package com.signumapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.signumapp.dto.MessageRequest;
import com.signumapp.service.MessageSocketService;

import java.util.Map;

/**
 * Controller class that handles real-time messaging using WebSocket communication.
 * Routes:
 * - /user: Send user conversations to a specific user by their user ID through a web socket.
 * - /conv: Send messages of a specific conversation to the connected users through a web socket.
 * - /sendMessage: Save a new message using a web socket.
 * - /deleteConversation: Delete a conversation by its unique conversation ID using a web socket.
 * - /deleteMessage: Delete a message by its unique message ID within a conversation using a web socket.
 */
@RequiredArgsConstructor
@Controller
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class MessageSocketController {
    private final MessageSocketService socketService;

    /**
     * Send user conversations to a specific user by their user ID through a web socket.
     *
     * @param userId The ID of the user for whom to send conversations.
     */
    @MessageMapping("/user")
    public void sendUserConversationByUserId(Long userId) {
        socketService.sendUserConversationByUserId(userId);
    }

    /**
     * Send messages of a specific conversation to the connected users through a web socket.
     *
     * @param conversationId The ID of the conversation for which to send messages.
     */
    @MessageMapping("/conv")
    public void sendMessagesByConversationId(Long conversationId) {
        socketService.sendMessagesByConversationId(conversationId);
    }

    /**
     * Save a new message using a web socket.
     *
     * @param message The MessageRequest object containing the message details to be saved.
     */
    @MessageMapping("/sendMessage")
    public void saveMessage(MessageRequest message) {
        socketService.saveMessage(message);
    }

    /**
     * Delete a conversation by its unique conversation ID using a web socket.
     *
     * @param payload A Map containing the conversationId, user1Id, and user2Id for the
     *                conversation to be deleted and notify listener.
     */
    @MessageMapping("/deleteConversation")
    public void deleteConversation(Map<String, Object> payload) {
        Long conversationId = (Long) payload.get("conversationId");
        Long user1 = (Long) payload.get("user1Id");
        Long user2 = (Long) payload.get("user2Id");
        socketService.deleteConversationByConversationId(conversationId);
        socketService.sendUserConversationByUserId(user1);
        socketService.sendUserConversationByUserId(user2);
    }

    /**
     * Delete a message by its unique message ID within a conversation using a web socket.
     *
     * @param payload A Map containing the conversationId and messageId for the message
     *                to be deleted and notify listener.
     */
    @MessageMapping("/deleteMessage")
public void deleteMessage(Map<String, Object> payload) {
    Number conversationIdObj = (Number) payload.get("conversationId");
    Number messageIdObj = (Number) payload.get("messageId");

    // Convert to Long, assuming they are expected to be Long values
    Long conversationId = conversationIdObj != null ? conversationIdObj.longValue() : null;
    Long messageId = messageIdObj != null ? messageIdObj.longValue() : null;

    if (conversationId != null && messageId != null) {
        socketService.deleteMessageByMessageId(conversationId, messageId);
    } else {
        System.out.println("Id can not be null");
    }
}

}


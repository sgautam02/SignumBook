package com.signumapp.service;

import com.signumapp.dto.MessageRequest;

public interface MessageSocketService {

    /**
     * Send user conversations to a specific user by their user ID through a web socket.
     *
     * @param userId The ID of the user for whom to send conversations.
     */
    void sendUserConversationByUserId(Long userId);

    /**
     * Send messages of a specific conversation to the connected users through a web socket.
     *
     * @param conversationId The ID of the conversation for which to send messages.
     */
    void sendMessagesByConversationId(Long conversationId);

    /**
     * Save a new message using a web socket.
     *
     * @param msg The MessageRequest object containing the message details to be saved.
     */
    void saveMessage(MessageRequest msg);

    /**
     * Delete a conversation by its unique conversation ID using a web socket.
     *
     * @param conversationId The ID of the conversation to be deleted.
     */
    void deleteConversationByConversationId(Long conversationId);

    /**
     * Delete a message by its unique message ID within a conversation using a web socket.
     *
     * @param conversationId The ID of the conversation to notify its listener.
     * @param messageId      The ID of the message to be deleted.
     */
    void deleteMessageByMessageId(Long conversationId, Long messageId);
}

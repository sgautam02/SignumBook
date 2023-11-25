package com.signumapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.signumapp.entity.Conversation;
import com.signumapp.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByConversation(Conversation conversation);

    void deleteAllByConversation(Conversation conversation);
}

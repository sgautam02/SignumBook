package com.signumapp.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.signumapp.dto.ApiResponse;
import com.signumapp.entity.Conversation;
import com.signumapp.entity.User;
import com.signumapp.repository.ConversationRepository;
import com.signumapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    UserRepository userRepository;
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<ApiResponse> findConversationIdByUser1IdAndUser2Id(Long user1Id, Long user2Id) {
        Long conversationId;
       
        User user1 = userService.getUserById(user1Id);
        User user2 = userService.getUserById(user2Id);
       
       
        if (user1 ==null || user2==null) {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .statusCode(200)
                            .status("Failed")
                            .reason("User not found")
                            .data(null)
                            .build(),
                    HttpStatus.OK
            );
        }

        Optional<Conversation> existingConversation = conversationRepository.findConversationByUsers(user1, user2);
        if (existingConversation.isPresent()) {
            conversationId = existingConversation.get().getConversationId();
        } else {
            Conversation newConversation = new Conversation();
            newConversation.setUser1(user1);
            newConversation.setUser2(user2);
            Conversation savedConversation = conversationRepository.save(newConversation);
            conversationId = savedConversation.getConversationId();
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .statusCode(200)
                        .status("Success")
                        .reason("OK")
                        .data(conversationId)
                        .build(),
                HttpStatus.OK
        );
    }

    
}

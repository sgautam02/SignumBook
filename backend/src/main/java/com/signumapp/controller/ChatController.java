package com.signumapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.signumapp.dto.ApiResponse;
import com.signumapp.service.ChatService;

import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class ChatController {

    @Autowired
    ChatService chatService;

    @GetMapping("/conversation/id")
    public ResponseEntity<ApiResponse> findConversationIdByUser1IdAndUser2Id(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        return chatService.findConversationIdByUser1IdAndUser2Id(user1Id, user2Id);
    }
    
}

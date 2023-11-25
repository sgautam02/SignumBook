package com.signumapp.service;

import org.springframework.http.ResponseEntity;

import com.signumapp.dto.ApiResponse;

public interface ChatService {

    ResponseEntity<ApiResponse> findConversationIdByUser1IdAndUser2Id(Long user1Id, Long user2Id);
    
}

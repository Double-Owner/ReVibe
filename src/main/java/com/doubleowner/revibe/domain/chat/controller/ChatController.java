package com.doubleowner.revibe.domain.chat.controller;

import com.doubleowner.revibe.domain.chat.dto.InviteUserDto;
import com.doubleowner.revibe.domain.chat.entity.ChatMessage;
import com.doubleowner.revibe.domain.chat.service.ChatService;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;


    @PostMapping("chats")//채팅방생성
    public void createChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String title) {
        chatService.create(userDetails.getUser(), title);

    }

    @PostMapping("chats/invites") // 사용자 채팅방에 추가
    public void invite(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody InviteUserDto inviteUserDto) {
        chatService.invite(userDetails.getUser(), inviteUserDto);
    }

    @MessageMapping("/chat/sendMessage/{roomId}")
    @SendTo("sub/chat/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable Long roomId, @Payload String message) {
        return chatService.send(roomId, message);
    }
}

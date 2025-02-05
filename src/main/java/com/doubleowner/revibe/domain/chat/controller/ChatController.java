package com.doubleowner.revibe.domain.chat.controller;

import com.doubleowner.revibe.domain.chat.dto.ChatMessageRequestDto;
import com.doubleowner.revibe.domain.chat.dto.ChatMessageResponseDto;
import com.doubleowner.revibe.domain.chat.dto.ChatRoomResponseDto;
import com.doubleowner.revibe.domain.chat.dto.InviteUserDto;
import com.doubleowner.revibe.domain.chat.service.ChatService;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping("/chats")//채팅방생성
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String title) {
        ChatRoomResponseDto chatRoomResponseDto = chatService.create(userDetails.getUser(), title);
        return new ResponseEntity<>(chatRoomResponseDto, HttpStatus.CREATED);

    }

    @PostMapping("/chats/invites") // 사용자 채팅방에 추가
    public void invite(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody InviteUserDto inviteUserDto) {
        chatService.invite(userDetails.getUser(), inviteUserDto);
    }

    @MessageMapping("/chat/sendMessage/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageRequestDto chatMessageRequestDto) {
        ChatMessageResponseDto send = chatService.send(roomId, chatMessageRequestDto.getMessage());
        messagingTemplate.convertAndSend("/sub/chat/" + roomId, send);
    }

    @GetMapping("/chatrooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getMessage(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long roomId, @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", required = false, defaultValue = "3") int size) {
        List<ChatMessageResponseDto> messages = chatService.getMessages(userDetails.getUser(), roomId, page, size);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/chats")
    public List<ChatRoomResponseDto> getChatRooms(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "3") int size) {
        List<ChatRoomResponseDto> chatRooms = chatService.getChatRooms(userDetails.getUser(), page, size);
        return chatRooms;

    }


}

package com.doubleowner.revibe.domain.chat.service;

import com.doubleowner.revibe.domain.chat.dto.ChatMessageResponseDto;
import com.doubleowner.revibe.domain.chat.dto.ChatRoomResponseDto;
import com.doubleowner.revibe.domain.chat.dto.InviteUserDto;
import com.doubleowner.revibe.domain.chat.entity.ChatMessage;
import com.doubleowner.revibe.domain.chat.entity.ChatRoom;
import com.doubleowner.revibe.domain.chat.entity.UserChat;
import com.doubleowner.revibe.domain.chat.repository.ChatMessageRepository;
import com.doubleowner.revibe.domain.chat.repository.ChatRoomRepository;
import com.doubleowner.revibe.domain.chat.repository.UserChatRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRepository userChatRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatRoomResponseDto create(User user, String title) {
        ChatRoom chatRoom = ChatRoom.builder().title(title).build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);


        UserChat userChat = UserChat.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();

        userChatRepository.save(userChat);
        return savedChatRoom.toDto();
    }

    public void invite(User user, InviteUserDto inviteUserDto) {
        User invitedUser = userRepository.findByEmail(inviteUserDto.getEmail()).orElseThrow(() -> new RuntimeException("사용자를 찾을수 없습니다"));
        ChatRoom chatRoom = chatRoomRepository.findById(inviteUserDto.getRoomId()).orElseThrow(() -> new RuntimeException("채팅방을 찾을수 없습니다"));
        UserChat build = UserChat.builder()
                .chatRoom(chatRoom)
                .user(invitedUser)
                .build();
        userChatRepository.save(build);
    }

    public ChatMessageResponseDto send(Long id, String message) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(() -> new RuntimeException("채팅방을 찾을수 없습니다"));
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .message(message)
                .build();
        ChatMessage save = chatMessageRepository.save(chatMessage);
        return ChatMessage.toDto(save);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getChatRooms(User user, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Slice<ChatRoom> chatRooms = userChatRepository.findChatRoomByUserId(user.getId(), pageRequest);
        List<ChatRoomResponseDto> list = chatRooms.stream().map(ChatRoom::toDto).toList();
        return list;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponseDto> getMessages(User user, Long roomId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ChatMessageResponseDto> chatMessageResponseDtos = chatMessageRepository.findAllById(roomId, pageRequest).stream().map(ChatMessage::toDto).toList();
        return chatMessageResponseDtos;

    }
}

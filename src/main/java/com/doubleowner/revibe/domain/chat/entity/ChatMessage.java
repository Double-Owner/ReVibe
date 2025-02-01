package com.doubleowner.revibe.domain.chat.entity;

import com.doubleowner.revibe.domain.chat.dto.ChatMessageResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;

    private String message;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static ChatMessageResponseDto toDto(ChatMessage chatMessage) {
        return ChatMessageResponseDto.builder()
                .id(chatMessage.id)
                .message(chatMessage.message)
                .createdAt(chatMessage.createdAt)
                .build();
    }
}

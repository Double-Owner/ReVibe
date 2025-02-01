package com.doubleowner.revibe.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ChatMessageResponseDto {
    private Long id;
    private String message;
    private LocalDateTime createdAt;
}

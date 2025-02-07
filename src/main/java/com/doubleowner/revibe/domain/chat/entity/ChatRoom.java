package com.doubleowner.revibe.domain.chat.entity;

import com.doubleowner.revibe.domain.chat.dto.ChatRoomResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "chatRoom")
    private List<UserChat> userChats;


    public ChatRoomResponseDto toDto() {
        return ChatRoomResponseDto.builder()
                .id(this.id)
                .title(this.title)
                .build();
    }
}

package com.doubleowner.revibe.domain.chat.repository;

import com.doubleowner.revibe.domain.chat.entity.ChatRoom;
import com.doubleowner.revibe.domain.chat.entity.UserChat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {

    @Query("SELECT uc.chatRoom FROM UserChat uc WHERE uc.user.id = :userId")
    Slice<ChatRoom> findChatRoomByUserId(@Param("userId") Long userId, Pageable pageable);
}

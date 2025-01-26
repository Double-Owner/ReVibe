package com.doubleowner.revibe.domain.chat.repository;

import com.doubleowner.revibe.domain.chat.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {
}

package com.doubleowner.revibe.global.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    /**
     * 생성일.
     */
    @CreatedDate
    @Column(updatable = false)
    @JsonProperty("create_at")
    private LocalDateTime createdAt;

    /**
     * 수정일.
     */
    @LastModifiedDate
    @JsonProperty("update_at")
    private LocalDateTime updatedAt;
}
package com.doubleowner.revibe.global.common.dto;

import com.doubleowner.revibe.global.exception.ParentException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponseDto {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponseDto> toResponseEntity(ParentException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(exception.toErrorResponseDto());
    }
}

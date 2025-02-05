package com.doubleowner.revibe.global.common.dto;

import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponseDto {
    private final ErrorCode error;
    private final String message;

    public static ResponseEntity<ErrorResponseDto> toResponseEntity(CustomException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(ErrorResponseDto.builder()
                        .error(exception.getErrorCode())
                        .message(exception.getMessage())
                        .build());
    }
}

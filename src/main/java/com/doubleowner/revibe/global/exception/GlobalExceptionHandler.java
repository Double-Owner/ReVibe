package com.doubleowner.revibe.global.exception;

import com.doubleowner.revibe.global.common.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {CommonException.class})
    public ResponseEntity<ErrorResponseDto> handleCommonException(CommonException exception) {
        return ErrorResponseDto.toResponseEntity(exception);
    }

    @ExceptionHandler(value = {ReviewException.class})
    public ResponseEntity<ErrorResponseDto> handleReviewException(ReviewException exception) {
        return ErrorResponseDto.toResponseEntity(exception);
    }

}

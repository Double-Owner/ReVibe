package com.doubleowner.revibe.global.exception;

import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {CommonException.class})
    public ResponseEntity<ErrorResponseDto> handleCommonException(CommonException exception) {
        return ErrorResponseDto.toResponseEntity(exception);
    }

    @ExceptionHandler(value = {UserException.class})
    public ResponseEntity<ErrorResponseDto> handleReviewException(UserException exception) {
        return ErrorResponseDto.toResponseEntity(exception);
    }

    @ExceptionHandler(value = {AccountException.class})
    public ResponseEntity<ErrorResponseDto> handleReviewException(AccountException exception) {
        return ErrorResponseDto.toResponseEntity(exception);
    }

    @ExceptionHandler(value = {ReviewException.class})
    public ResponseEntity<ErrorResponseDto> handleReviewException(ReviewException exception) {
        return ErrorResponseDto.toResponseEntity(exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponseBody<>(e.getMessage()));
    }

}

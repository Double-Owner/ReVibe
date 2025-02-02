package com.doubleowner.revibe.global.exception;

import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {CommonException.class})
    public ResponseEntity<ErrorResponseDto> handleCommonException(CommonException exception) {
        return ErrorResponseDto.toResponseEntity(exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidException(MethodArgumentNotValidException e) {

        ErrorResponseDto message = ErrorResponseDto.builder()
                .error(e.getStatusCode().toString())
                .code("Failed Validation") // todo 하드코딩된 code 수정
                .message(e.getBindingResult().getFieldError().getDefaultMessage())
                .build();
        return new ResponseEntity<>(message, e.getStatusCode());

    }

}

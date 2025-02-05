package com.doubleowner.revibe.global.exception;

import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.ILLEGAL_ARGUMENT;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ErrorResponseDto> handleCommonException(CustomException exception) {
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
                .error(ILLEGAL_ARGUMENT) // todo 하드코딩된 code 수정
                .message(e.getBindingResult().getFieldError().getDefaultMessage())
                .build();
        return new ResponseEntity<>(message, e.getStatusCode());

    }

}

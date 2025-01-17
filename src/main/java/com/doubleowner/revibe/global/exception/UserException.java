package com.doubleowner.revibe.global.exception;

import com.doubleowner.revibe.global.common.dto.ErrorResponseDto;
import com.doubleowner.revibe.global.exception.errorCode.UserErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends ParentException {

    private final UserErrorCode errorCode;

    public UserException(UserErrorCode errorCode) {
        this.errorCode = errorCode;

    }

    @Override
    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }

    @Override
    public ErrorResponseDto toErrorResponseDto() {
        return ErrorResponseDto.builder()
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .code(errorCode.toString())
                .message(errorCode.getDetail())
                .build();
    }

}

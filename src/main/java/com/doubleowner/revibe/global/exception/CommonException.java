package com.doubleowner.revibe.global.exception;

import com.doubleowner.revibe.global.common.dto.ErrorResponseDto;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import org.springframework.http.HttpStatus;

public class CommonException extends ParentException{

    private final ErrorCode errorCode;
    private final String msg;

    public CommonException(ErrorCode errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public CommonException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.msg = errorCode.getDetail();
    }

    @Override
    public HttpStatus getHttpStatus(){
        return errorCode.getHttpStatus();
    }

    @Override
    public ErrorResponseDto toErrorResponseDto() {
        return ErrorResponseDto.builder()
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .code(errorCode.toString())
                .massage(msg)
                .build();
    }
}

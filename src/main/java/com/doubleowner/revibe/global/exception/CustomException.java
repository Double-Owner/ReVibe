package com.doubleowner.revibe.global.exception;


import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private ErrorCode errorCode;

    public CustomException(ErrorCode e) {
        super(e.getDetail());
        this.errorCode = e;
    }
}

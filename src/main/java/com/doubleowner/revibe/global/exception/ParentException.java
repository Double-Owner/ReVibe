package com.doubleowner.revibe.global.exception;

import com.doubleowner.revibe.global.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;

public abstract class ParentException extends RuntimeException{

    public abstract HttpStatus getHttpStatus();

    public abstract ErrorResponseDto toErrorResponseDto();
}

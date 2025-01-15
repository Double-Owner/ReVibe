package com.doubleowner.revibe.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ImageErrorCode {

    FAILED_UPLOAD_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 이미지를 저장할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}

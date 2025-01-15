package com.doubleowner.revibe.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode {
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND,"리뷰를 찾을수 없습니다");

    private final HttpStatus httpStatus;
    private final String detail;


}

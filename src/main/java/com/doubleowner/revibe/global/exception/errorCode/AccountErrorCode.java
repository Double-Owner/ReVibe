package com.doubleowner.revibe.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AccountErrorCode {

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "계좌 정보를 찾을 수 없습니다."),
    INVALID_ACCOUNT_NUMBER(HttpStatus.NOT_FOUND, "해당 계좌는 사용자의 계좌가 아닙니다"),
    REJECT_ACCOUNT_DELETE(HttpStatus.FORBIDDEN, "계좌 삭제에 실패했습니다. ID를 다시 입력해주세요.");

    private final HttpStatus httpStatus;
    private final String detail;
}

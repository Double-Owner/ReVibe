package com.doubleowner.revibe.global.exception.errorCode;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode {

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    EMAIL_EXIST_LOCAL(HttpStatus.BAD_REQUEST, "일반 계정으로 등록된 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다." ),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}

package com.doubleowner.revibe.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통 에러 코드
    // 400 BAD REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"요청이 올바르지 않습니다."),
    // 401 UNAUTHORIZED
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED,"인증에 실패했습니다."),
    // 403 FORBIDDEN
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "권한이 없는 접근입니다."),
    // 404 NOT FOUND
    NOT_FOUND_VALUE(HttpStatus.NOT_FOUND, "해당 정보가 없습니다."),

    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 값입니다."),

    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 값입니다"),

    // 이미지 에러 코드
    FAILED_UPLOAD_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지를 저장할 수 없습니다."),

    FAILED_DELETE_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지를 삭제 할 수 없습니다."),

    // 쿠폰 발급 에러 코드
    NOT_FOUND_COUPON(HttpStatus.NOT_FOUND, "쿠폰이 존재하지 않습니다."),

    INVALID_COUPON_CODE(HttpStatus.FORBIDDEN,"유효하지 않은 쿠폰입니다."),

    ALREADY_USED_COUPON(HttpStatus.BAD_REQUEST,  "이미 사용된 쿠폰입니다."),

    ALREADY_ISSUED_COUPON(HttpStatus.FORBIDDEN, "이미 발급된 쿠폰입니다."),

    GENERATE_MORE_THAN_MAX_COUNT(HttpStatus.FORBIDDEN, "쿠폰이 소진되었습니다.");




    private final HttpStatus httpStatus;
    private final String detail;
}

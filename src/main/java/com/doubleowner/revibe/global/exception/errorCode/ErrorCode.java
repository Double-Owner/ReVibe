package com.doubleowner.revibe.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통 에러 코드
    // 400 BAD REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청이 올바르지 않습니다."),
    // 401 UNAUTHORIZED
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    // 403 FORBIDDEN
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "권한이 없는 접근입니다."),
    // 404 NOT FOUND
    NOT_FOUND_VALUE(HttpStatus.NOT_FOUND, "해당 정보가 없습니다."),

    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 값입니다."),

    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 값입니다"),

    //계좌 에러 코드
    NOT_FOUND_ACCOUNT(HttpStatus.NOT_FOUND, "계좌를 찾을 수 없습니다"),

    INVALID_ACCOUNT(HttpStatus.NOT_FOUND, "이미 존재하거나 유효 하지 않은 계좌 입니다"),

    // 이미지 에러 코드
    FAILED_UPLOAD_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지를 저장할 수 없습니다."),

    FAILED_DELETE_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지를 삭제 할 수 없습니다."),

    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "올바른 확장자가 아닙니다."),


    // 쿠폰 발급 에러 코드
    NOT_FOUND_COUPON(HttpStatus.NOT_FOUND, "쿠폰이 존재하지 않습니다."),

    INVALID_COUPON_CODE(HttpStatus.FORBIDDEN, "유효하지 않은 쿠폰입니다."),

    ALREADY_USED_COUPON(HttpStatus.BAD_REQUEST, "이미 사용된 쿠폰입니다."),

    ALREADY_ISSUED_COUPON(HttpStatus.FORBIDDEN, "이미 발급된 쿠폰입니다."),

    GENERATE_MORE_THAN_MAX_COUNT(HttpStatus.FORBIDDEN, "쿠폰이 소진되었습니다."),

    INVALID_BID_PRICE(HttpStatus.BAD_REQUEST, "구매 입찰가는 즉시 판매가보다 낮아야 합니다."),

    INVALID_SELL_PRICE(HttpStatus.BAD_REQUEST, "판매 입찰가는 즉시 구매가보다 낮아야 합니다."),

    // 자신이 등록한 물건을 구매할 수 없음
    CANNOT_PURCHASE_OWN_ITEM(HttpStatus.BAD_REQUEST, "자신의 물건은 구매할 수 없습니다."),


    ALREADY_EXIST_SIZE(HttpStatus.BAD_REQUEST, "이미 동일한 사이즈가 존재합니다."),
    // 결제 관련 에러 코드 추가
    PAYMENT_PROCESS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제 도중 오류가 발생했습니다."),

    INSUFFICIENT_POINTS(HttpStatus.BAD_REQUEST, "포인트가 부족합니다");


    private final HttpStatus httpStatus;

    private final String detail;
}

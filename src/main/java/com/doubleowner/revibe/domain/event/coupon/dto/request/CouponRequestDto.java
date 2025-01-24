package com.doubleowner.revibe.domain.event.coupon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CouponRequestDto {

//    @NotBlank(message = "쿠폰이름 입력은 필수 값입니다") //유니크한 키 이름 설정
    private String name;

//    @NotNull(message = "할인 가격 입력은 필수값입니다")
    private int price;

//    @NotNull(message = "쿠폰 전체 개수 입력은 필수값입니다")
    private int totalQuantity;

    private LocalDateTime issuedStart;
    private LocalDateTime issuedEnd;

}

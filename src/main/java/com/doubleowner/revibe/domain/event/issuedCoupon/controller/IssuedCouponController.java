package com.doubleowner.revibe.domain.event.issuedCoupon.controller;

import com.doubleowner.revibe.domain.event.coupon.dto.response.IssuedCouponResponseDto;
import com.doubleowner.revibe.domain.event.coupon.service.IssuedCouponService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/issuedCoupons")
@RequiredArgsConstructor
public class IssuedCouponController {

    private final IssuedCouponService issuedCouponService;

    /**
     * 쿠폰 발급 요청하는 컨트롤러
     * @return
     */
    @PostMapping("/{id}")
    public ResponseEntity<CommonResponseBody<IssuedCouponResponseDto>> issueCoupon(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){

        IssuedCouponResponseDto issuedCouponResponseDto = issuedCouponService.issueCoupon(id, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseBody<>("쿠폰 등록이 완료되었습니다.", issuedCouponResponseDto), HttpStatus.CREATED);
    }


}

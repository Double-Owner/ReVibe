package com.doubleowner.revibe.domain.event.coupon.controller;

import com.doubleowner.revibe.domain.event.coupon.dto.request.CouponRequestDto;
import com.doubleowner.revibe.domain.event.coupon.dto.response.CouponResponseDto;
import com.doubleowner.revibe.domain.event.coupon.service.CouponService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 생성
    @PostMapping
    public ResponseEntity<CommonResponseBody<CouponResponseDto>> createCoupon(
            @RequestBody CouponRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CouponResponseDto couponResponseDto = couponService.createCoupon(userDetails.getUser(), dto);

        return new ResponseEntity<>(new CommonResponseBody<>("쿠폰 등록이 완료되었습니다.", couponResponseDto), HttpStatus.CREATED);

    }

    // 쿠폰 조회
    @GetMapping
    public ResponseEntity<CommonResponseBody<List<CouponResponseDto>>> findCoupon(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam int page, @RequestParam int size
    ) {

        List<CouponResponseDto> couponResponseDto = couponService.findCoupons(userDetails.getUser(), page, size);

        return new ResponseEntity<>(new CommonResponseBody<>("쿠폰 조회를 성공하였습니다.", couponResponseDto), HttpStatus.OK);
    }

    // 쿠폰 수정
    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponseBody<CouponResponseDto>> updateCoupon(
            @RequestBody CouponRequestDto dto, @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CouponResponseDto couponResponseDto = couponService.updateCoupon(userDetails.getUser(), id, dto);

        return new ResponseEntity<>(new CommonResponseBody<>("쿠폰 정보를 수정하였습니다.", couponResponseDto), HttpStatus.OK);
    }

    // 쿠폰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseBody<Void>> deleteCoupon(
            @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        couponService.deleteCoupon(userDetails.getUser(), id);

        return new ResponseEntity<>(new CommonResponseBody<>("쿠폰 삭제가 완료되었습니다."), HttpStatus.OK);
    }
}

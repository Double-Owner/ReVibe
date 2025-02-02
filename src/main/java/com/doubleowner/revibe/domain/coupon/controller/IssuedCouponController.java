package com.doubleowner.revibe.domain.coupon.controller;

import com.doubleowner.revibe.domain.coupon.dto.response.IssuedCouponResponseDto;
import com.doubleowner.revibe.domain.coupon.service.IssuedCouponService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        IssuedCouponResponseDto issuedCouponResponseDto = issuedCouponService.issuedCoupon(id, userDetails.getUser());

        return new ResponseEntity<>(new CommonResponseBody<>("쿠폰 등록이 완료되었습니다.", issuedCouponResponseDto), HttpStatus.CREATED);
    }

    /**
     * 발급된 쿠폰 조회
     * @param userDetails
     * @return
     */
    @GetMapping()
    public ResponseEntity<CommonResponseBody<List<IssuedCouponResponseDto>>> getIssuedCoupons(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) {
        List<IssuedCouponResponseDto> coupons = issuedCouponService.getUserCoupons(userDetails.getUser(), page, size);

        return new ResponseEntity<>(new CommonResponseBody<>("발급된 쿠폰을 조회하였습니다.", coupons), HttpStatus.OK);
    }

    /**
     * 쿠폰 사용 현황
     * @param id
     * @param userDetails
     * @return
     */
    @PatchMapping("/{id}/used")
    public ResponseEntity<CommonResponseBody<IssuedCouponResponseDto>> usedCoupon(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        issuedCouponService.usedCoupon(id, userDetails.getUser());
        return new ResponseEntity<>(new CommonResponseBody<>("쿠폰 사용이 완료되었습니다."), HttpStatus.OK);
    }

}

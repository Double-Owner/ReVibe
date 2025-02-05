package com.doubleowner.revibe.domain.cart.controller;

import com.doubleowner.revibe.domain.cart.dto.response.CartResponseDto;
import com.doubleowner.revibe.domain.cart.service.CartService;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 담기
    @PostMapping
    public ResponseEntity<CommonResponseBody<CartResponseDto>> addCart(
            @RequestParam Long optionId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        User loginUser = userDetails.getUser();
        CartResponseDto responseDto = cartService.addCart(loginUser, optionId);

        return ResponseEntity.status(HttpStatus.CREATED).body((new CommonResponseBody<>("상품을 장바구니에 등록했습니다.",responseDto)));
    }
    // 장바구니 조회
    @GetMapping
    public ResponseEntity<CommonResponseBody<List<CartResponseDto>>> getCarts(
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        User loginUser = userDetails.getUser();
        List<CartResponseDto> responseDtos = cartService.getMyCarts(loginUser);

        return ResponseEntity.status(HttpStatus.OK).body((new CommonResponseBody<>("내 장바구니를 조회했습니다.",responseDtos)));
    }

    // 장바구니 삭제
    @DeleteMapping
    public ResponseEntity<CommonResponseBody> deleteCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long optionId)
    {
        User loginUser = userDetails.getUser();
        cartService.deleteCart(loginUser,optionId);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseBody<>("장바구니에서 상품을 제거했습니다."));
    }
}



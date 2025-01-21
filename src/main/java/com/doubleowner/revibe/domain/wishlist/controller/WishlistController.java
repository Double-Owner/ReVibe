package com.doubleowner.revibe.domain.wishlist.controller;

import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.wishlist.dto.WishlistResponseDto;
import com.doubleowner.revibe.domain.wishlist.service.WishlistService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WishlistController {

    private final WishlistService wishlistService;

    // 관심상품 등록.해제
    @PostMapping("/items/{itemId}/wishlists")
    public ResponseEntity<CommonResponseBody<?>> doWishlist(
            @PathVariable Long itemId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            )
    {
        User loginUser = userDetails.getUser();
        if(wishlistService.doWishlist(loginUser,itemId)){
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseBody<>("해당 상품을 관심상품으로 등록했습니다."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseBody<>("해당상품을 관심상품에서 해제했습니다."));
    }

    // 관심상품 목록 조회
    @GetMapping("/wishlists")
    public ResponseEntity<CommonResponseBody<List<WishlistResponseDto>>> getWishlist(
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        User loginUser = userDetails.getUser();
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseBody<>("관심상품 목록",wishlistService.findWishlists(loginUser)));
    }
}

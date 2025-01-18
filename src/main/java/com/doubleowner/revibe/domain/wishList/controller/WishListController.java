package com.doubleowner.revibe.domain.wishList.controller;

import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.wishList.dto.WishListResponseDto;
import com.doubleowner.revibe.domain.wishList.service.WishListService;
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
public class WishListController {

    private final WishListService wishListService;

    // 관심상품 등록.해제
    @PostMapping("/items/{itemId}/wishlists")
    public ResponseEntity<CommonResponseBody> doWishList(
            @PathVariable Long itemId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            )
    {
        User loginUser = userDetails.getUser();
        if(wishListService.doWishList(loginUser,itemId)){
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseBody("해당 상품을 관심상품으로 등록했습니다."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseBody("해당상품을 관심상품에서 해제했습니다."));
    }

    // 관심상품 목록 조회
    @GetMapping("/wishlists")
    public ResponseEntity<CommonResponseBody<List<WishListResponseDto>>> getWishList(
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        User loginUser = userDetails.getUser();
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseBody<>("관심상품 목록",wishListService.findWishLists(loginUser)));
    }
}

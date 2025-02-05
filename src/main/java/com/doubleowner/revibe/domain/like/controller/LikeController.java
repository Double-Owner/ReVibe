package com.doubleowner.revibe.domain.like.controller;

import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.like.dto.LikeResponseDto;
import com.doubleowner.revibe.domain.like.service.LikeService;
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
public class LikeController {

    private final LikeService likeService;

    // 관심상품 등록.해제
    @PostMapping("/items/{itemId}/likes")
    public ResponseEntity<CommonResponseBody<?>> doLike(
            @PathVariable Long itemId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            )
    {
        User loginUser = userDetails.getUser();
        if(likeService.doLike(loginUser,itemId)){
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseBody<>("해당 상품을 관심상품으로 등록했습니다."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseBody<>("해당상품을 관심상품에서 해제했습니다."));
    }

    // 관심상품 목록 조회
    @GetMapping("/likes")
    public ResponseEntity<CommonResponseBody<List<LikeResponseDto>>> getLikes(
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        User loginUser = userDetails.getUser();
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseBody<>("관심상품 목록", likeService.findLikes(loginUser)));
    }
}

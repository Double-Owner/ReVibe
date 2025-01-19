package com.doubleowner.revibe.domain.item.controller;

import com.doubleowner.revibe.domain.item.dto.request.ItemRequestDto;
import com.doubleowner.revibe.domain.item.dto.request.ItemSearchRequestDto;
import com.doubleowner.revibe.domain.item.dto.request.ItemUpdateRequestDto;
import com.doubleowner.revibe.domain.item.dto.response.ItemResponseDto;
import com.doubleowner.revibe.domain.item.service.ItemService;
import com.doubleowner.revibe.domain.review.dto.ReviewResponseDto;
import com.doubleowner.revibe.domain.review.service.ReviewService;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ReviewService reviewService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<CommonResponseBody<ItemResponseDto>> createItem(
            @Valid @ModelAttribute ItemRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User loginUser = userDetails.getUser();
        ItemResponseDto responseDto = itemService.createItem(loginUser, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseBody<>("상품을 등록했습니다", responseDto));
    }

    // 상품 수정
    @PatchMapping("/{itemId}")
    public ResponseEntity<CommonResponseBody<ItemResponseDto>> updateItem(
            @Valid @ModelAttribute ItemUpdateRequestDto requestDto,
            @PathVariable Long itemId
    ) {
        ItemResponseDto responseDto = itemService.modifyItem(itemId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body((new CommonResponseBody<>("상품이 수정 되었습니다.", responseDto)));
    }

    // 상품 전체 조회
    @GetMapping
    public ResponseEntity<CommonResponseBody<Page<ItemResponseDto>>> getItems(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size,
            @RequestBody ItemSearchRequestDto requestDto
    ) {
        Page<ItemResponseDto> responseDtos = itemService.getAllItems(page, size, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body((new CommonResponseBody<>("상품들을 조회했습니다.", responseDtos)));
    }

    // 상품 단건 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<CommonResponseBody<ItemResponseDto>> getItemDetail(@PathVariable Long itemId) {
        ItemResponseDto responseDto = itemService.getItem(itemId);
        return ResponseEntity.status(HttpStatus.OK).body((new CommonResponseBody<>("상품을 조회했습니다.", responseDto)));
    }

    @GetMapping("/{itemId}/reviews")
    public ResponseEntity<CommonResponseBody<List<ReviewResponseDto>>> getItemReviews(
            @PathVariable Long itemId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) {
        List<ReviewResponseDto> responseDto = reviewService.findItemReviews(itemId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body((new CommonResponseBody<>("리뷰를 조회 했습니다.", responseDto)));

    }
}
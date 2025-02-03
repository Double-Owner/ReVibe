package com.doubleowner.revibe.domain.review.controller;

import com.doubleowner.revibe.domain.review.dto.ReviewRequestDto;
import com.doubleowner.revibe.domain.review.dto.ReviewResponseDto;
import com.doubleowner.revibe.domain.review.dto.UpdateReviewRequestDto;
import com.doubleowner.revibe.domain.review.service.ReviewService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponseBody<ReviewResponseDto>> writeReview(@ModelAttribute ReviewRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto review = reviewService.write(requestDto, userDetails.getUser());
        return new ResponseEntity<>(new CommonResponseBody<>("리뷰가 등록되었습니다.", review), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CommonResponseBody<List<ReviewResponseDto>>> findReviews(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                                   @RequestParam(value = "size", required = false, defaultValue = "3") int size) {
        List<ReviewResponseDto> read = reviewService.findReview(userDetails.getUser(), page, size);
        return new ResponseEntity<>(new CommonResponseBody<>("리뷰가 조회 되었습니다", read), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponseBody<Void>> updateReview(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @ModelAttribute UpdateReviewRequestDto updateReviewRequestDto) {
        reviewService.updateReview(id, userDetails, updateReviewRequestDto);
        return new ResponseEntity<>(new CommonResponseBody<>("리뷰가 수정되었습니다."), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseBody<Void>> deleteReview(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(id, userDetails.getUser());
        return new ResponseEntity<>(new CommonResponseBody<>("리뷰가 삭제되었습니다."), HttpStatus.OK);
    }
}

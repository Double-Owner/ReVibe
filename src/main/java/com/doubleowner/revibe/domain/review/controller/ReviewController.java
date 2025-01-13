package com.doubleowner.revibe.domain.review.controller;

import com.doubleowner.revibe.domain.review.dto.ReviewRequestDto;
import com.doubleowner.revibe.domain.review.dto.ReviewResponseDto;
import com.doubleowner.revibe.domain.review.service.ReviewService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(value = "/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponseBody<ReviewResponseDto>> writeReview(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart ReviewRequestDto reviewRequestDto, @RequestPart(name = "image") MultipartFile file) {
        ReviewResponseDto review = reviewService.write(reviewRequestDto, file, userDetails.getUser());
        return new ResponseEntity<>(new CommonResponseBody<>("리뷰가 등록되었습니다.", review), HttpStatus.CREATED);
    }

    @GetMapping("/reviews")
    public ResponseEntity<CommonResponseBody<List<ReviewResponseDto>>> findReviews(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ReviewResponseDto> read = reviewService.findReview(userDetails.getUser());
        return new ResponseEntity<>(new CommonResponseBody<>("리뷰가 조회 되었습니다", read), HttpStatus.OK);
    }
}

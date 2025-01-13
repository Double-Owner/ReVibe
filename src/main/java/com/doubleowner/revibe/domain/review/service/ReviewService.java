package com.doubleowner.revibe.domain.review.service;

import com.doubleowner.revibe.domain.execution.entity.Execution;
import com.doubleowner.revibe.domain.execution.repository.ExecutionRepository;
import com.doubleowner.revibe.domain.payment.entity.Payment;
import com.doubleowner.revibe.domain.payment.repository.PaymentRepository;
import com.doubleowner.revibe.domain.review.dto.ReviewRequestDto;
import com.doubleowner.revibe.domain.review.dto.ReviewResponseDto;
import com.doubleowner.revibe.domain.review.entity.Review;
import com.doubleowner.revibe.domain.review.repository.ReviewRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final ExecutionRepository executionRepository;
    private final S3Uploader s3Uploader;

    public ReviewResponseDto write(ReviewRequestDto reviewRequestDto, MultipartFile file, User user) {
        Payment payment = paymentRepository.findById(reviewRequestDto.getPaymentId()).orElseThrow(() -> new RuntimeException());
        if (!user.getEmail().equals(payment.getBuy().getUser().getEmail())) {
            throw new RuntimeException("내가 구매한 상품이 아닙니다.");
        }
        Execution execution = executionRepository.findById(reviewRequestDto.getExecutionId()).orElseThrow(() -> new RuntimeException("내역을 찾을 수 없습니다"));
        String image;
        try {
            image = s3Uploader.upload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Review review = Review.builder()
                .starRate(reviewRequestDto.getStarRate())
                .title(reviewRequestDto.getTitle())
                .content(reviewRequestDto.getContent())
                .reviewImage(image)
                .execution(execution)
                .item(execution.getSell().getOptions().getItem())
                .build();
        Review save = reviewRepository.save(review);

        return Review.todto(save);
    }
}

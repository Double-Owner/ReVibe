package com.doubleowner.revibe.domain.review.service;

import com.doubleowner.revibe.domain.execution.entity.Execution;
import com.doubleowner.revibe.domain.execution.repository.ExecutionRepository;
import com.doubleowner.revibe.domain.payment.entity.Payment;
import com.doubleowner.revibe.domain.payment.repository.PaymentRepository;
import com.doubleowner.revibe.domain.review.dto.ReviewRequestDto;
import com.doubleowner.revibe.domain.review.dto.ReviewResponseDto;
import com.doubleowner.revibe.domain.review.dto.UpdateReviewRequestDto;
import com.doubleowner.revibe.domain.review.entity.Review;
import com.doubleowner.revibe.domain.review.repository.ReviewRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import com.doubleowner.revibe.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final ExecutionRepository executionRepository;
    private final S3Uploader s3Uploader;

    @Transactional
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
                .user(user)
                .build();
        Review save = reviewRepository.save(review);

        return Review.toDto(save);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findReview(User user) {
        List<Review> reviews = reviewRepository.findByUserId(user.getId());
        return reviews.stream().map(Review::toDto).toList();
    }

    @Transactional
    public void updateReview(Long id, UserDetailsImpl userDetails, UpdateReviewRequestDto updateReviewRequestDto, MultipartFile file) {
        Review review = reviewRepository.findReviewByIdAndUser_Id(id, userDetails.getUser().getId()).orElseThrow(() -> new RuntimeException("작성하신 리뷰를 찾을수 없습니다."));

        if (file != null) {
            try {
                String imageUrl = s3Uploader.upload(file);
                review.update(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        review.update(updateReviewRequestDto);

    }

    @Transactional
    public void deleteReview(Long id, User user) {
        Review review = reviewRepository.findReviewByIdAndUser_Id(id, user.getId()).orElseThrow(() -> new RuntimeException("작성하신 리뷰를 찾을수 없습니다."));
        reviewRepository.delete(review);
    }
}

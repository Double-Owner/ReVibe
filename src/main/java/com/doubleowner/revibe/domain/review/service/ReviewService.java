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
import com.doubleowner.revibe.global.exception.ImageException;
import com.doubleowner.revibe.global.exception.errorCode.ImageErrorCode;
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

        Payment payment = paymentRepository.findByPaymentId(reviewRequestDto.getPaymentId()).orElseThrow(() -> new RuntimeException());

        if (!user.getEmail().equals(payment.getBuy().getUser().getEmail())) {
            throw new RuntimeException("내가 구매한 상품이 아닙니다.");
        }

        Execution execution = executionRepository.findExecutionById(reviewRequestDto.getExecutionId()).orElseThrow(() -> new RuntimeException("내역을 찾을 수 없습니다"));

        String image = uploadImage(file);

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

        return toDto(save);
    }


    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findReview(User user) {

        List<Review> reviewsByUserId = reviewRepository.findReviewsByUserId(user.getId());
        return reviewsByUserId.stream().map(this::toDto).toList();
    }

    @Transactional
    public void updateReview(Long id, UserDetailsImpl userDetails, UpdateReviewRequestDto updateReviewRequestDto, MultipartFile file) {

        Review review = reviewRepository.findMyReview(id, userDetails.getUser().getId());

        if (file != null) {
            this.reUploadImage(review, file);
        }

        review.update(updateReviewRequestDto);

    }

    @Transactional
    public void deleteReview(Long id, User user) {

        Review review = reviewRepository.findMyReview(id, user.getId());

        reviewRepository.delete(review);
    }

    /**
     * DTO로 변경해주는 메소드
     *
     * @param review
     * @return
     */
    private ReviewResponseDto toDto(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .starRate(review.getStarRate())
                .createdAt(review.getCreatedAt())
                .image(review.getReviewImage())
                .build();
    }

    /**
     * 이미지 업로드 메소드
     *
     * @param file
     * @return
     */
    private String uploadImage(MultipartFile file) {

        if (file == null) {
            return null;
        }

        try {
            return s3Uploader.upload(file);

        } catch (IOException e) {
            throw new ImageException(ImageErrorCode.FAILED_UPLOAD_IMAGE);
        }
    }

    /**
     * 이미지 재업로드 메소드 (기존 이미지 삭제 후 업로드)
     *
     * @param review
     * @param file
     */
    private void reUploadImage(Review review, MultipartFile file) {

        try {
            s3Uploader.deleteImage(review.getReviewImage());
            String imageUrl = uploadImage(file);
            review.update(imageUrl);

        } catch (IOException e) {
            throw new ImageException(ImageErrorCode.FAILED_UPLOAD_IMAGE);
        }

    }

    public List<ReviewResponseDto> findItemReviews(Long itemId) {
        List<Review> reviews = reviewRepository.findReviewsByItem_Id(itemId);
        return reviews.stream().map(this::toDto).toList();
    }
}

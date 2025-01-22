package com.doubleowner.revibe.domain.review.service;

import com.doubleowner.revibe.domain.execution.entity.Execution;
import com.doubleowner.revibe.domain.execution.repository.ExecutionRepository;
import com.doubleowner.revibe.domain.review.dto.ReviewRequestDto;
import com.doubleowner.revibe.domain.review.dto.ReviewResponseDto;
import com.doubleowner.revibe.domain.review.dto.UpdateReviewRequestDto;
import com.doubleowner.revibe.domain.review.entity.Review;
import com.doubleowner.revibe.domain.review.repository.ReviewRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import com.doubleowner.revibe.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ExecutionRepository executionRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    private static final int TEXT_ONLY_POINT = 100;
    private static final int TEXT_IMAGE_POINT = 500;
    private static final int DIFFERENCE_POINT = 400;

    @Transactional
    public ReviewResponseDto write(ReviewRequestDto reviewRequestDto, User user) {


        Execution execution = executionRepository.findExecutionById(reviewRequestDto.getPaymentId(), user.getEmail())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE, "내역을 찾을 수 없습니다"));
        String image = null;
        int point = TEXT_ONLY_POINT;
        if (reviewRequestDto.getImage() != null) {
            image = uploadImage(reviewRequestDto.getImage());
            point = TEXT_IMAGE_POINT;
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
        user.addPoint(point);
        userRepository.save(user);


        return toDto(save);
    }


    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findReview(User user) {

        List<Review> reviewsByUserId = reviewRepository.findReviewsByUserId(user.getId());
        return reviewsByUserId.stream().map(this::toDto).toList();
    }

    @Transactional
    public void updateReview(Long id, UserDetailsImpl userDetails, UpdateReviewRequestDto updateReviewRequestDto) {

        Review review = reviewRepository.findMyReview(id, userDetails.getUser().getId());

        // 이미지 관련 처리
        handlePoint(review, updateReviewRequestDto.getImage());

        // 나머지 필드 업데이트
        review.update(updateReviewRequestDto);

    }

    @Transactional
    public void deleteReview(Long id, User user) {
        Review review = reviewRepository.findMyReview(id, user.getId());
        if (review.getReviewImage() != null) {
            review.getUser().minusPoint(TEXT_IMAGE_POINT);
        } else {
            review.getUser().minusPoint(TEXT_ONLY_POINT);
        }
        reviewRepository.delete(review);
    }

    private void handlePoint(Review review, MultipartFile newImage) {
        try {
            if (newImage != null) {
                if (review.getReviewImage() == null) {
                    review.getUser().addPoint(DIFFERENCE_POINT);
                }
                // 새 이미지 업로드
                reUploadImage(review, newImage);

            } else {
                if (review.getReviewImage() != null) {
                    s3Uploader.deleteImage(review.getReviewImage());
                    review.deleteImage();
                    review.getUser().minusPoint(DIFFERENCE_POINT);
                }
            }
        } catch (IOException e) {
            throw new CommonException(ErrorCode.FAILED_UPLOAD_IMAGE);
        }
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

        try {
            return s3Uploader.upload(file);

        } catch (IOException e) {
            throw new CommonException(ErrorCode.FAILED_UPLOAD_IMAGE);
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
            if (review.getReviewImage() != null) {
                s3Uploader.deleteImage(review.getReviewImage());
            }

            String imageUrl = uploadImage(file);
            review.update(imageUrl);

        } catch (IOException e) {
            throw new CommonException(ErrorCode.FAILED_UPLOAD_IMAGE);
        }

    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findItemReviews(Long itemId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Review> reviews = reviewRepository.findReviewsByItemId(itemId, pageable).stream().toList();
        return reviews.stream().map(this::toDto).toList();
    }
}

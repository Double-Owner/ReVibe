package com.doubleowner.revibe.domain.review.service;

import com.doubleowner.revibe.domain.payment.entity.Payment;
import com.doubleowner.revibe.domain.payment.repository.PaymentRepository;
import com.doubleowner.revibe.domain.point.PointService;
import com.doubleowner.revibe.domain.review.dto.ReviewRequestDto;
import com.doubleowner.revibe.domain.review.dto.ReviewResponseDto;
import com.doubleowner.revibe.domain.review.dto.UpdateReviewRequestDto;
import com.doubleowner.revibe.domain.review.entity.Review;
import com.doubleowner.revibe.domain.review.repository.ReviewRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.service.ImageService;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.NOT_FOUND_VALUE;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final PointService pointService;
    private final ImageService imageService;


    @Transactional
    public ReviewResponseDto write(ReviewRequestDto reviewRequestDto, User user) {

        Payment payment = paymentRepository.findByPaymentId(reviewRequestDto.getPaymentId(), user.getEmail())
                .orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));

        String image = null;

        if (hasImage(reviewRequestDto.getImage())) {
            image = imageService.uploadImage(image, reviewRequestDto.getImage());
        }

        Review review = ReviewRequestDto.toEntity(reviewRequestDto, payment, user, image);

        Review savedReview = reviewRepository.save(review);

        pointService.addReviewPoint(user, image);

        return Review.toDto(savedReview);
    }


    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findReview(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Slice<Review> reviewsByUserId = reviewRepository.findReviewsByUserId(user.getId(), pageable);
        return reviewsByUserId.stream().map(Review::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findItemReviews(Long itemId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Slice<Review> reviews = reviewRepository.findReviewsByItemId(itemId, pageable);
        return reviews.stream().map(Review::toDto).toList();
    }

    @Transactional
    public void updateReview(Long id, UserDetailsImpl userDetails, UpdateReviewRequestDto updateReviewRequestDto) {

        Review review = getMyReview(id, userDetails.getUser());

        // 이미지 관련 처리
        String image = null;
        if (hasImage(updateReviewRequestDto.getImage())) {
            image = imageService.uploadImage(review.getReviewImage(), updateReviewRequestDto.getImage());
        }
        pointService.handlePoint(userDetails.getUser(), review.getReviewImage(), updateReviewRequestDto.getImage());
        // 필드 업데이트
        review.update(updateReviewRequestDto, image);

    }

    @Transactional
    public void deleteReview(Long id, User user) {
        Review review = getMyReview(id, user);
        if (review.getReviewImage() != null) {
            imageService.deleteImage(review.getReviewImage());
        }
        pointService.deletePoint(user, review.getReviewImage());
        reviewRepository.delete(review);
    }

    private Review getMyReview(Long id, User user) {
        return reviewRepository.findMyReview(id, user.getId());
    }

    private static boolean hasImage(MultipartFile multipartFile) {
        return multipartFile != null && !multipartFile.isEmpty();
    }


}

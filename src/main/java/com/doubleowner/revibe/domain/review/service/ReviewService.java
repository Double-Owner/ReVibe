package com.doubleowner.revibe.domain.review.service;

import com.doubleowner.revibe.domain.execution.entity.Execution;
import com.doubleowner.revibe.domain.execution.repository.ExecutionRepository;
import com.doubleowner.revibe.domain.point.PointService;
import com.doubleowner.revibe.domain.review.dto.ReviewRequestDto;
import com.doubleowner.revibe.domain.review.dto.ReviewResponseDto;
import com.doubleowner.revibe.domain.review.dto.UpdateReviewRequestDto;
import com.doubleowner.revibe.domain.review.entity.Review;
import com.doubleowner.revibe.domain.review.repository.ReviewRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.service.ImageService;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ExecutionRepository executionRepository;
    private final PointService pointService;
    private final ImageService imageService;


    @Transactional
    public ReviewResponseDto write(ReviewRequestDto reviewRequestDto, User user) {

        Execution execution = executionRepository.findExecutionById(reviewRequestDto.getExecutionId(), user.getEmail())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE, "내역을 찾을 수 없습니다"));

        String image = null;

        if (hasImage(reviewRequestDto.getImage())) {
            image = imageService.UploadImage(image, reviewRequestDto.getImage());
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

        Review savedReview = reviewRepository.save(review);

        pointService.addReviewPoint(user, image);

        return toDto(savedReview);
    }


    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findReview(User user) {

        List<Review> reviewsByUserId = reviewRepository.findReviewsByUserId(user.getId());
        return reviewsByUserId.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findItemReviews(Long itemId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Slice<Review> reviews = reviewRepository.findReviewsByItemId(itemId, pageable);
        return reviews.stream().map(this::toDto).toList();
    }

    @Transactional
    public void updateReview(Long id, UserDetailsImpl userDetails, UpdateReviewRequestDto updateReviewRequestDto) {

        Review review = getMyReview(id, userDetails.getUser());

        // 이미지 관련 처리
        String image = null;
        if (hasImage(updateReviewRequestDto.getImage())) {
            image = imageService.UploadImage(review.getReviewImage(), updateReviewRequestDto.getImage());
        }
        pointService.handlePoint(userDetails.getUser(), review.getReviewImage(), updateReviewRequestDto.getImage());
        // 필드 업데이트
        review.update(updateReviewRequestDto, image);

    }

    @Transactional
    public void deleteReview(Long id, User user) {
        Review review = getMyReview(id, user);
        pointService.deletePoint(user, review.getReviewImage());
        reviewRepository.delete(review);
    }

    private Review getMyReview(Long id, User user) {
        return reviewRepository.findMyReview(id, user.getId());
    }

    private static boolean hasImage(MultipartFile multipartFile) {
        return multipartFile != null && !multipartFile.isEmpty();
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

}

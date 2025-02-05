package com.doubleowner.revibe.domain.review.repository;

import com.doubleowner.revibe.domain.review.entity.Review;
import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.NOT_FOUND_VALUE;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findReviewByIdAndUserId(Long id, Long userId);

    default Review findMyReview(Long id, Long userId) {
        return findReviewByIdAndUserId(id, userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));
    }

    @EntityGraph(attributePaths = {"user", "item", "payment"})
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId")
    Slice<Review> findReviewsByUserId(@Param("userId") Long userId, Pageable pageable);

    Slice<Review> findReviewsByItemId(Long itemId, Pageable pageable);

}

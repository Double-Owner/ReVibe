package com.doubleowner.revibe.domain.review.repository;

import com.doubleowner.revibe.domain.review.entity.Review;
import com.doubleowner.revibe.global.exception.ReviewException;
import com.doubleowner.revibe.global.exception.errorCode.ReviewErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findReviewByIdAndUserId(Long id, Long userId);

    default Review findMyReview(Long id, Long userId) {
        return findReviewByIdAndUserId(id, userId).orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
    }

    @EntityGraph(attributePaths = {"user", "item", "execution"})
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId")
    List<Review> findReviewsByUserId(@Param("userId") Long userId);

    Page<Review> findReviewsByItemId(Long itemId, Pageable pageable);
}

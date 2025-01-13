package com.doubleowner.revibe.domain.review.repository;

import com.doubleowner.revibe.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    List<Review> findByUserId(Long id);

    Optional<Review> findReviewByIdAndUser_Id(Long id, Long userId);
}

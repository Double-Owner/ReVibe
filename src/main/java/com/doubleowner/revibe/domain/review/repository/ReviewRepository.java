package com.doubleowner.revibe.domain.review.repository;

import com.doubleowner.revibe.domain.review.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findReviewByIdAndUser_Id(Long id, Long userId);

    @EntityGraph(attributePaths = {"user", "item", "execution"})
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId")
    List<Review> findReviewsByUserId(@Param("userId") Long userId);

}

package com.doubleowner.revibe.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {

    private Long executionId;

    private Long paymentId;

    private Double starRate;

    private String title;

    private String content;
}

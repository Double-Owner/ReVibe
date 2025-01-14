package com.doubleowner.revibe.domain.review.dto;

import lombok.Getter;

@Getter
public class UpdateReviewRequestDto {

    private Double starRate;

    private String title;

    private String content;

}

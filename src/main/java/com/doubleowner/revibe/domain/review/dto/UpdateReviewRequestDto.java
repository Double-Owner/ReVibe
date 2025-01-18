package com.doubleowner.revibe.domain.review.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateReviewRequestDto {

    @NotNull(message = "별점은 필수 값입니다.")
    @DecimalMin(value = "0.0", message = "별점은 0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "별점은 5 이하이어야 합니다.")
    private Double starRate;

    @NotNull(message = "제목은 필수 값입니다.")
    private String title;

    @NotNull(message = "내용은 필수 값입니다.")
    private String content;

}

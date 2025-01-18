package com.doubleowner.revibe.domain.review.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {

    @NotNull(message = "Execution ID는 필수 값입니다")
    private Long executionId;

    @NotNull(message = "Payment ID는 필수 값입니다")
    private Long paymentId;

    @NotNull(message = "별점은 필수 값입니다.")
    @DecimalMin(value = "0.0", message = "별점은 0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "별점은 5 이하이어야 합니다.")
    private Double starRate;

    @NotNull(message = "제목은 필수 값입니다.")
    private String title;

    @NotNull(message = "내용은 필수 값입니다.")
    private String content;
}

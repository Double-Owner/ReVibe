package com.doubleowner.revibe.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@Builder
public class ReviewRequestDto {

    @NotNull(message = "payment ID는 필수 값입니다")
    private Long paymentId;

    @NotNull(message = "별점은 필수 값입니다.")
    @Min(value = 0, message = "별점은 0 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하이어야 합니다.")
    private Integer starRate;

    @NotNull(message = "제목은 필수 값입니다.")
    private String title;

    @NotNull(message = "내용은 필수 값입니다.")
    private String content;

    private MultipartFile image;
}

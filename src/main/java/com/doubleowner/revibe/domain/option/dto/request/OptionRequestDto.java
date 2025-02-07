package com.doubleowner.revibe.domain.option.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OptionRequestDto {

    @NotNull(message = "사이즈 입력은 필수입니다.")
    private int size;

}

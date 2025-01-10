package com.doubleowner.revibe.domain.option.dto.response;

import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.entity.Size;
import lombok.Getter;

@Getter
public class OptionResponseDto {

    private final Long optionId;

    private final Long itemId;

    private final Size size;

    public OptionResponseDto(Long optionId, Long itemId, Size size) {
        this.optionId = optionId;
        this.itemId = itemId;
        this.size = size;
    }

    public static OptionResponseDto toDto(Option option) {
        return new OptionResponseDto(option.getId(),
                option.getItem().getId(),
                option.getSize());
    }
}

package com.doubleowner.revibe.domain.buyBid.dto;

import com.doubleowner.revibe.domain.buyBid.entity.BuyBid;
import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.enumType.BidStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BuyBidRequestDto {

    @NotNull
    @Min(20000)
    private Long price;

    @NotNull
    private Long optionId;

    public BuyBid toEntity(Option option , User user) {
        return BuyBid.builder()
                .price(this.price)
                .buyStatus(BidStatus.ONPROGRESS)
                .user(user)
                .option(option)
                .build();
    }

}
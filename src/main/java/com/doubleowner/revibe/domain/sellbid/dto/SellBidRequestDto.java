package com.doubleowner.revibe.domain.sellbid.dto;

import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.sellbid.entity.SellBid;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.enumType.BidStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SellBidRequestDto {

    @NotNull
    private Long optionId;

    @NotNull
    private Long price;

    @NotNull
    @Min(1)
    private Long amount;

    public SellBid toEntity(Option option, User user) {
        return SellBid.builder()
                .price(this.price)
                .amount(this.amount)
                .status(BidStatus.ONPROGRESS)
                .user(user)
                .options(option)
                .build();
    }
}

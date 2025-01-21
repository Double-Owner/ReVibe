package com.doubleowner.revibe.domain.sellbid.dto;

import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.sellbid.entity.SellBid;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.enumType.BidStatus;
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
    private Long amount;

    @NotNull
    private Long endedAt;

    public SellBid toEntity(Option option, User user) {
        return SellBid.builder()
                .price(this.price)
                .amount(this.amount)
                .status(BidStatus.ONPROGRESS)
                .endedAt(LocalDateTime.now().plusDays(this.endedAt))
                .user(user)
                .options(option)
                .build();
    }
}

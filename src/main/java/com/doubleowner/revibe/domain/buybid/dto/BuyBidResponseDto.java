package com.doubleowner.revibe.domain.buybid.dto;

import com.doubleowner.revibe.domain.buybid.entity.BuyBid;
import com.doubleowner.revibe.domain.option.entity.Size;
import com.doubleowner.revibe.global.common.enumType.BidStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BuyBidResponseDto {
    private Long buyBidId;

    private Size optionSize;

    private Long price;

    private BidStatus status;

    public static BuyBidResponseDto toDto(BuyBid buyBid) {
        return BuyBidResponseDto.builder()
                .buyBidId(buyBid.getId())
                .optionSize(buyBid.getOption().getSize())
                .price(buyBid.getPrice())
                .status(buyBid.getBuyStatus())
                .build();
    }
}

package com.doubleowner.revibe.domain.sellbid.dto;

import com.doubleowner.revibe.domain.option.entity.Size;
import com.doubleowner.revibe.domain.sellbid.entity.SellBid;
import com.doubleowner.revibe.global.common.enumType.BidStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SellBidResponseDto {

    private Long sellBidId;

    private Size size;

    private Long price;

    private Long amount;

    private BidStatus status;

    private LocalDateTime createdAt;

    public static SellBidResponseDto toDto(SellBid sellBid) {
        return SellBidResponseDto.builder()
                .sellBidId(sellBid.getId())
                .size(sellBid.getOptions().getSize())
                .price(sellBid.getPrice())
                .amount(sellBid.getAmount())
                .status(sellBid.getStatus())
                .createdAt(sellBid.getCreatedAt())
                .build();

    }
}

package com.doubleowner.revibe.domain.sellBid.dto;

import com.doubleowner.revibe.domain.option.entity.Size;
import com.doubleowner.revibe.domain.sellBid.entity.SellBid;
import com.doubleowner.revibe.global.common.enumType.BidStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Builder
public class SellBidResponseDto {

    private Long sellBidId;

    private Size size;

    private Long price;

    private Long amount;

    private BidStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;


    public static SellBidResponseDto toDto(SellBid sellBid) {
        return SellBidResponseDto.builder()
                .sellBidId(sellBid.getId())
                .size(sellBid.getOptions().getSize())
                .price(sellBid.getPrice())
                .amount(sellBid.getAmount())
                .status(sellBid.getStatus())
                .startedAt(sellBid.getStartedAt())
                .endedAt(sellBid.getEndedAt())
                .build();

    }
}

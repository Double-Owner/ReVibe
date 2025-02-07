package com.doubleowner.revibe.domain.execution.dto;

import com.doubleowner.revibe.domain.execution.entity.Execution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecutionResponseDto {

    private Long id;
    private Long sellBidId;
    private Long buyBidId;

    public static ExecutionResponseDto toDto(Execution execution) {
        return ExecutionResponseDto.builder()
                .id(execution.getId())
                .sellBidId(execution.getSell().getId())
                .buyBidId(execution.getBuyBid().getId())
                .build();
    }
}

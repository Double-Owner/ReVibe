package com.doubleowner.revibe.domain.execution.dto;

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
    private Long paymentId;

}

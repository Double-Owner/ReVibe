package com.doubleowner.revibe.domain.execution.service;

import com.doubleowner.revibe.domain.buybid.entity.BuyBid;
import com.doubleowner.revibe.domain.buybid.repository.BuyBidRepository;
import com.doubleowner.revibe.domain.execution.dto.ExecutionResponseDto;
import com.doubleowner.revibe.domain.execution.entity.Execution;
import com.doubleowner.revibe.domain.execution.repository.ExecutionRepository;
import com.doubleowner.revibe.domain.payment.entity.Payment;
import com.doubleowner.revibe.domain.payment.repository.PaymentRepository;
import com.doubleowner.revibe.domain.sellbid.entity.SellBid;
import com.doubleowner.revibe.domain.sellbid.repository.SellBidRepository;
import com.doubleowner.revibe.domain.user.entity.Role;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.FORBIDDEN_ACCESS;
import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.NOT_FOUND_VALUE;

@Service
@RequiredArgsConstructor
public class ExecutionService {

    private final SellBidRepository sellBidRepository;
    private final BuyBidRepository buyBidRepository;
    private final ExecutionRepository executionRepository;

    public ExecutionResponseDto createExecution(Long sellBidId, Long buyBidId) {
        // 1. SellBid와 BuyBid 조회 ->
        SellBid sellBid = sellBidRepository.findById(sellBidId).orElseThrow(() -> new CommonException(NOT_FOUND_VALUE));
        BuyBid buyBid = buyBidRepository.findById(buyBidId).orElseThrow(() -> new CommonException(NOT_FOUND_VALUE));

        // 2. Execution 생성
        Execution execution = Execution.builder()
                .sell(sellBid)
                .buyBid(buyBid)
                .build();

        // 3. Execution 저장
        executionRepository.save(execution);

        return toDto(execution);
    }

    private ExecutionResponseDto toDto(Execution execution) {
        return ExecutionResponseDto.builder()
                .id(execution.getId())
                .sellBidId(execution.getSell().getId())
                .buyBidId(execution.getBuyBid().getId())
                .build();
    }
}

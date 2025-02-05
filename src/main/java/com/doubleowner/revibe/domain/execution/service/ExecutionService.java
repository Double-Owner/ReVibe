package com.doubleowner.revibe.domain.execution.service;

import com.doubleowner.revibe.domain.buybid.entity.BuyBid;
import com.doubleowner.revibe.domain.buybid.repository.BuyBidRepository;
import com.doubleowner.revibe.domain.execution.dto.ExecutionResponseDto;
import com.doubleowner.revibe.domain.execution.entity.Execution;
import com.doubleowner.revibe.domain.execution.entity.ExecutionStatus;
import com.doubleowner.revibe.domain.execution.repository.ExecutionRepository;
import com.doubleowner.revibe.domain.sellbid.entity.SellBid;
import com.doubleowner.revibe.domain.sellbid.repository.SellBidRepository;
import com.doubleowner.revibe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.NOT_FOUND_VALUE;

@Service
@RequiredArgsConstructor
public class ExecutionService {

    private final SellBidRepository sellBidRepository;
    private final BuyBidRepository buyBidRepository;
    private final ExecutionRepository executionRepository;

    @Transactional
    public ExecutionResponseDto createExecution(Long sellBidId, Long buyBidId) {
        // 1. SellBid와 BuyBid 조회 ->
        SellBid sellBid = sellBidRepository.findById(sellBidId).orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));
        BuyBid buyBid = buyBidRepository.findById(buyBidId).orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));

        // 2. Execution 생성
        Execution execution = Execution.builder()
                .sell(sellBid)
                .buyBid(buyBid)
                .status(ExecutionStatus.WAITING_FOR_PAYMENT)
                .build();

        // 3. Execution 저장
        executionRepository.save(execution);

        return ExecutionResponseDto.toDto(execution);
    }

    public List<ExecutionResponseDto> findExecution(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Execution> executions = executionRepository.findAllToSlice(pageable);

        return executions.map(ExecutionResponseDto::toDto).toList();
    }
}

package com.doubleowner.revibe.domain.execution.service;

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

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.FORBIDDEN_ACCESS;
import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.NOT_FOUND_VALUE;

@Service
@RequiredArgsConstructor
public class ExecutionService {

    private final SellBidRepository sellBidRepository;
    private final PaymentRepository paymentRepository;
    private final ExecutionRepository executionRepository;

    public ExecutionResponseDto createExecution(User user, Long sellBidId, Long paymentId) {

        // 어드민 권한을 가진 유저만 체결할수 있음
        // todo 스케줄러나 다른 기술 활용하여 수정
        if (!user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new CommonException(FORBIDDEN_ACCESS);
        }

        // 1. SellBid와 Payment 조회
        SellBid sellBid = sellBidRepository.findById(sellBidId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_VALUE));

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new CommonException(NOT_FOUND_VALUE));

        // 2. Execution 생성
        Execution execution = Execution.builder()
                .sell(sellBid)
                .payment(payment)
                .build();

        // 3. Execution 저장
        executionRepository.save(execution);

        return toDto(execution);

    }

    private ExecutionResponseDto toDto(Execution execution) {
        return ExecutionResponseDto.builder()
                .id(execution.getId())
                .sellBidId(execution.getSell().getId())
                .paymentId(execution.getPayment().getId())
                .build();
    }
}

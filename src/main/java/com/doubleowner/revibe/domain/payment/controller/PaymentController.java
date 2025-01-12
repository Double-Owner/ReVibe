package com.doubleowner.revibe.domain.payment.controller;

import com.doubleowner.revibe.domain.payment.dto.CardPaymentRequestDto;
import com.doubleowner.revibe.domain.payment.dto.PaymentResponseDto;
import com.doubleowner.revibe.domain.payment.service.PaymentService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Value("${secret.key}")
    private String secretKey;


    @PostMapping
    public ResponseEntity<CommonResponseBody<PaymentResponseDto>> requestCardPayment(@RequestBody CardPaymentRequestDto cardPaymentRequestDto) throws Exception {
        PaymentResponseDto paymentResponseDto = paymentService.payCard(cardPaymentRequestDto);
        return new ResponseEntity<>(new CommonResponseBody<>("주문이 완료 되었습니다.", paymentResponseDto), HttpStatus.CREATED);

    }

}
package com.doubleowner.revibe.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PaymentRequestDto {

    private String orderId;

    private Long amount;

    private String cardNumber;

    private String cardExpirationYear;

    private String cardExpirationMonth;

    private String cardPassword;

    private String customerIdentityNumber;

    private String paymentType;

    private String paymentKey;
}

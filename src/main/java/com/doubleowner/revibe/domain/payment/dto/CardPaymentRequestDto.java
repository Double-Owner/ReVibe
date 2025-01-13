package com.doubleowner.revibe.domain.payment.dto;

import lombok.Getter;

@Getter
public class CardPaymentRequestDto {
    private String orderId;
    private Long amount;
    private String cardNumber;
    private String cardExpirationYear;
    private String cardExpirationMonth;
    private String cardPassword;
    private String customerIdentityNumber;
    private Long buyBidId;
}

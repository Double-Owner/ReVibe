package com.doubleowner.revibe.domain.payment.service;

import com.doubleowner.revibe.domain.coupon.entity.IssuedCoupon;
import com.doubleowner.revibe.domain.coupon.repository.IssuedCouponRepository;
import com.doubleowner.revibe.domain.execution.entity.Execution;
import com.doubleowner.revibe.domain.execution.repository.ExecutionRepository;
import com.doubleowner.revibe.domain.payment.dto.CardPaymentRequestDto;
import com.doubleowner.revibe.domain.payment.dto.PaymentResponseDto;
import com.doubleowner.revibe.domain.payment.entity.PayMethod;
import com.doubleowner.revibe.domain.payment.entity.PayStatus;
import com.doubleowner.revibe.domain.payment.entity.Payment;
import com.doubleowner.revibe.domain.payment.repository.PaymentRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${toss.secret-key}")
    private String cardSecretKey;

    private final PaymentRepository paymentRepository;
    private final RestTemplate template;
    private final ExecutionRepository executionRepository;
    private final UserRepository userRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    // 카드 결제
    @Transactional
    public PaymentResponseDto payCard(User user, CardPaymentRequestDto cardPaymentRequestDto) {
        User findUser = userRepository.findByEmailOrElseThrow(user.getEmail());

        Execution execution = executionRepository.findExecutionById(cardPaymentRequestDto.getExecutionId(), user.getEmail()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE));

        Long price = execution.getBuyBid().getPrice();
        if (cardPaymentRequestDto.getUsePoint() != 0) {
            price -= cardPaymentRequestDto.getUsePoint();
            findUser.minusPoint(cardPaymentRequestDto.getUsePoint());
        }
        if (cardPaymentRequestDto.getUseCouponId().longValue() != 0) {
            IssuedCoupon useCoupon = issuedCouponRepository.findByIdAndUser(cardPaymentRequestDto.getUseCouponId(), user).orElseThrow(() -> new CommonException(ErrorCode.INVALID_COUPON_CODE));
            price -= useCoupon.getCoupon().getPrice();
            useCoupon.usedCoupon();

        }

        JSONObject paymentData = sendPaymentRequest(cardPaymentRequestDto, "https://api.tosspayments.com/v1/payments/key-in", price);
        Map<String, Object> parsedData = parsingData(paymentData);

        Payment payment = Payment.builder()
                .execution(execution)
                .payMethod(PayMethod.CREDIT_CARD)
                .tossId((String) parsedData.get("paymentKey"))
                .payStatus(PayStatus.PAY_SUCCESS)
                .build();

        return toDto(paymentRepository.save(payment));

    }

    private JSONObject sendPaymentRequest(CardPaymentRequestDto cardPaymentRequestDto, String url, Long price) {

        JSONObject requestData = new JSONObject();
        requestData.put("orderId", "ORDER_ID" + UUID.randomUUID() + cardPaymentRequestDto.getExecutionId());
        requestData.put("amount", price);
        requestData.put("cardNumber", cardPaymentRequestDto.getCardNumber());
        requestData.put("cardExpirationYear", cardPaymentRequestDto.getCardExpirationYear());
        requestData.put("cardExpirationMonth", cardPaymentRequestDto.getCardExpirationMonth());
        requestData.put("cardPassword", cardPaymentRequestDto.getCardPassword());
        requestData.put("customerIdentityNumber", cardPaymentRequestDto.getCustomerIdentityNumber());
        return sendRequest(requestData, cardSecretKey, url);
    }


    // json 파싱
    private Map<String, Object> parsingData(JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        result.put("amount", jsonObject.get("amount"));
        result.put("paymentKey", jsonObject.get("paymentKey"));
        result.put("method", jsonObject.get("method"));
        return result;

    }

    @Transactional
    public PaymentResponseDto payToss(String secretKey, String paymentType, String orderId, String paymentKey, String amount) {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("paymentType", paymentType);
        jsonBody.put("orderId", orderId);
        jsonBody.put("paymentKey", paymentKey);
        jsonBody.put("amount", amount);
        JSONObject object;

        object = sendRequest(jsonBody, secretKey, "https://api.tosspayments.com/v1/payments/confirm");
        // 받아온 paymentData 파싱하기
        Map<String, Object> stringObjectMap = parsingData(object);

        // 파싱한 paymentData 값 DB에 넣기
        // todo : 프론트단 구현시 buyBid 객체 추가
        Payment payment = Payment.builder()
                .payMethod(PayMethod.TOSS_PAY)
                .tossId(stringObjectMap.get("paymentKey").toString())
                .payStatus(PayStatus.PAY_SUCCESS)
                .build();

        Payment save = paymentRepository.save(payment);

        return toDto(save);
    }

    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) {
        HttpHeaders headers = createHeader(secretKey);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestData.toJSONString(), headers);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = template.exchange(urlString, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "결제 요청 중 오류가 발생했습니다: " + e.getMessage());
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "결제 요청 실패: " + responseEntity.getBody());
        }

        try {
            return (JSONObject) new JSONParser().parse(responseEntity.getBody());
        } catch (ParseException e) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "결제 응답 파싱 실패: " + e.getMessage());
        }
    }


    private HttpHeaders createHeader(String secretKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getHistory(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<Payment> paymentHistory = paymentRepository.findPaymentByUserId(user.getId(), pageable);
        return paymentHistory.stream().map(this::toDto).toList();


    }

    private PaymentResponseDto toDto(Payment payment) {
        return PaymentResponseDto
                .builder()
                .paymentId(payment.getId())
                .payMethod(payment.getPayMethod().name())
                .build();
    }

}
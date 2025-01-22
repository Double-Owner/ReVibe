package com.doubleowner.revibe.domain.payment.service;

import com.doubleowner.revibe.domain.buybid.entity.BuyBid;
import com.doubleowner.revibe.domain.buybid.repository.BuyBidRepository;
import com.doubleowner.revibe.domain.payment.dto.CardPaymentRequestDto;
import com.doubleowner.revibe.domain.payment.dto.PaymentResponseDto;
import com.doubleowner.revibe.domain.payment.entity.PayMethod;
import com.doubleowner.revibe.domain.payment.entity.PayStatus;
import com.doubleowner.revibe.domain.payment.entity.Payment;
import com.doubleowner.revibe.domain.payment.repository.PaymentRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${toss.secret-key}")
    private String cardSecretKey;

    private final PaymentRepository paymentRepository;
    private final BuyBidRepository buyBidRepository;

    // 카드 결제
    @Transactional
    public PaymentResponseDto payCard(CardPaymentRequestDto cardPaymentRequestDto) {
        BuyBid buyBid = buyBidRepository.findById(cardPaymentRequestDto.getBuyBidId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE, "요청하신 구매 요청건이 없습니다."));

        JSONObject paymentData = sendPaymentRequest(cardPaymentRequestDto, "https://api.tosspayments.com/v1/payments/key-in");
        Map<String, Object> parsedData = parsingData(paymentData);

        Payment payment = Payment.builder()
                .buy(buyBid)
                .payMethod(PayMethod.CREDIT_CARD)
                .tossId((String) parsedData.get("paymentKey"))
                .payStatus(PayStatus.PAY_SUCCESS)
                .build();

        return toDto(paymentRepository.save(payment));

    }

    private JSONObject sendPaymentRequest(CardPaymentRequestDto cardPaymentRequestDto, String url) {

        JSONObject requestData = new JSONObject();
        requestData.put("orderId", cardPaymentRequestDto.getOrderId());
        requestData.put("amount", cardPaymentRequestDto.getAmount());
        requestData.put("cardNumber", cardPaymentRequestDto.getCardNumber());
        requestData.put("cardExpirationYear", cardPaymentRequestDto.getCardExpirationYear());
        requestData.put("cardExpirationMonth", cardPaymentRequestDto.getCardExpirationMonth());
        requestData.put("cardPassword", cardPaymentRequestDto.getCardPassword());
        requestData.put("customerIdentityNumber", cardPaymentRequestDto.getCustomerIdentityNumber());

        try {
            return sendRequest(requestData, cardSecretKey, url);
        } catch (IOException e) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "유효하지 않은 요청입니다.");
        }
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

        try {
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
        } catch (IOException e) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "결제에 실패했습니다.");
        }


    }

    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
        HttpURLConnection connection = createConnection(secretKey, urlString);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "올바르지 않은 요청입니다.");
        }
    }

    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
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
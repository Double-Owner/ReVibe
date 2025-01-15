package com.doubleowner.revibe.domain.payment.service;

import com.doubleowner.revibe.domain.buyBid.entity.BuyBid;
import com.doubleowner.revibe.domain.buyBid.repository.BuyBidRepository;
import com.doubleowner.revibe.domain.payment.dto.CardPaymentRequestDto;
import com.doubleowner.revibe.domain.payment.dto.PaymentResponseDto;
import com.doubleowner.revibe.domain.payment.entity.PayMethod;
import com.doubleowner.revibe.domain.payment.entity.PayStatus;
import com.doubleowner.revibe.domain.payment.entity.Payment;
import com.doubleowner.revibe.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${secret.key}")
    private String secretKey;

    private final PaymentRepository paymentRepository;
    private final BuyBidRepository buyBidRepository;

    // 카드 결제
    public PaymentResponseDto payCard(CardPaymentRequestDto cardPaymentRequestDto) throws IOException, ParseException {
        BuyBid buyBid = buyBidRepository.findById(cardPaymentRequestDto.getBuyBidId()).orElseThrow(() -> new RuntimeException("요청하신 주문건이 없습니다."));

        JSONObject paymentData = getPaymentData(cardPaymentRequestDto);

        // 받아온 paymentData 파싱하기
        Map<String, Object> stringObjectMap = parsingData(paymentData);

        // 파싱한 paymentData와 buyBid 객체  값 DB에 넣기
        Payment payment = Payment.builder()
                .buy(buyBid)
                .payMethod(PayMethod.CREDIT_CARD)
                .tossId(stringObjectMap.get("paymentKey").toString())
                .payStatus(PayStatus.PAY_SUCCESS)
                .build();

        Payment save = paymentRepository.save(payment);

        return Payment.toDto(save);

    }

    // 토스에서 데이터 가져오가
    private JSONObject getPaymentData(CardPaymentRequestDto cardPaymentRequestDto) throws IOException, ParseException {

        URL url = new URL("https://api.tosspayments.com/v1/payments/key-in");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        JSONObject obj = new JSONObject();
        obj.put("orderId", cardPaymentRequestDto.getOrderId());
        obj.put("amount", cardPaymentRequestDto.getAmount());
        obj.put("cardNumber", cardPaymentRequestDto.getCardNumber());
        obj.put("cardExpirationYear", cardPaymentRequestDto.getCardExpirationYear());
        obj.put("cardExpirationMonth", cardPaymentRequestDto.getCardExpirationMonth());
        obj.put("cardPassword", cardPaymentRequestDto.getCardPassword());
        obj.put("customerIdentityNumber", cardPaymentRequestDto.getCustomerIdentityNumber());

        OutputStream os = connection.getOutputStream();
        os.write(obj.toString().getBytes(StandardCharsets.UTF_8));
        InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();


        System.out.println(jsonObject);
        return jsonObject;
    }

    // json 파싱
    private Map<String, Object> parsingData(JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        result.put("amount", jsonObject.get("amount"));
        result.put("paymentKey", jsonObject.get("paymentKey"));
        result.put("method", jsonObject.get("method"));
        return result;

    }
}
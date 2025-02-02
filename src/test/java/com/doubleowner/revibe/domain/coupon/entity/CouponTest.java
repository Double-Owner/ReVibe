package com.doubleowner.revibe.domain.coupon.entity;

import com.doubleowner.revibe.domain.coupon.service.CouponService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class CouponTest {

    @Autowired
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 중복 발급 방지 테스트")
    void CouponNotDuplicated() throws InterruptedException {
        // given
        long userId = 1L;
        Long couponId = 1L;
        int numberOfThreads = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);

        for (int i = 1; i <= numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    // 분산락 적용 메서드 호출
                    couponService.createCoupon(userId, couponId);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); // 모든 스레드가 작업을 마칠 때까지 대기
        executorService.shutdown(); // 스레드 풀 종료

        // when
        int issuedCouponCount = couponService.findCoupons(userId, couponId); // 발급된 쿠폰 개수 조회

        // then
        assertThat(issuedCouponCount).isEqualTo(1);
    }

}
package com.doubleowner.revibe.domain.event.issuedCoupon.service;

import com.doubleowner.revibe.domain.coupon.entity.Coupon;
import com.doubleowner.revibe.domain.coupon.repository.CouponRepository;
import com.doubleowner.revibe.domain.coupon.service.IssuedCouponService;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IssuedCouponServiceTest {

    @Autowired
    private IssuedCouponService issuedCouponService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    public void SetUp() {
        List<User> users = new ArrayList<>();
        for(int i = 1; i<=100; i++){
            users.add(new User("zzanggu" + i, "qwer" + i));
        }
        userRepository.saveAll(users);

        Coupon createCoupon = Coupon.builder()
                .name("FIRST_COME_FIRST_SERVED")
                .price(5000)
                .totalQuantity(100)
                .build();

        couponRepository.save(createCoupon);
    }

    @Test
    @DisplayName(value = "분산락을 이용하여 쿠폰 획득하는 테스트입니다.")
    void getCouponWithLock() throws InterruptedException {

        int threadCount = 100; // 스레드 수 (쿠폰 발급 신청 유저 수)
        ExecutorService eventExecutor = Executors.newFixedThreadPool(5); //스레드 pool의 개수
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            int userId = i;
            eventExecutor.submit(() -> {
                    issuedCouponService.issuedCoupon(1L, userRepository.findById((long) userId).get());
                    countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        int totalQuantity = couponRepository.findById(1L).get().getTotalQuantity();

        assertEquals(0, totalQuantity);

    }
}
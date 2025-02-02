package com.doubleowner.revibe.global.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class LockAspect {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;

    @Around("@annotation(com.doubleowner.revibe.global.aop.DistributedLock) && args(id, ..)")

    public Object lock(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + method.getName() + ":" + id;

        RLock couponLock = redissonClient.getLock(key);

        boolean lock = false;
        try {
            lock = couponLock.tryLock(5L, 10L, TimeUnit.SECONDS);
            if (!lock) {
                System.out.println("락 획득 실패 !");
            }
            return joinPoint.proceed(); //어노테이션 메서드 실행

        } finally {
            if (lock) {
                couponLock.unlock();
            }
        }
    }
}

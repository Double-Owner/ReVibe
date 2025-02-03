package com.doubleowner.revibe.global.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LockAspect {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;

    @Around("@annotation(com.doubleowner.revibe.global.aop.DistributedLock) && args(id, ..)")
    public Object lock(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

//        String key = REDISSON_LOCK_PREFIX + method.getName() + ":" + id;
        String key = distributedLock.key().isEmpty() ? method.getName() : distributedLock.key();
        String LockKey = REDISSON_LOCK_PREFIX + key;

        RLock couponLock = redissonClient.getLock(LockKey);

        boolean lock = false;
        try {
            lock = couponLock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!lock) {
                log.info("락 획득 실패 !");
                throw new IllegalStateException("실패");
            }
            return joinPoint.proceed(); //어노테이션 메서드 실행

        } finally {
            log.info("락 무한대기 뿌뿌 ~~");
            if (lock && couponLock.isHeldByCurrentThread()) {
                try {
                    couponLock.unlock();
                } catch (IllegalMonitorStateException e){
                    log.info(e.getMessage());
                }
            }
        }
    }
}

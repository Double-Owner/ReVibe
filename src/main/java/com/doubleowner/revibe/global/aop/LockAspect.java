package com.doubleowner.revibe.global.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LockAspect {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final StandardEvaluationContext context = new StandardEvaluationContext();

    @Around("@annotation(com.doubleowner.revibe.global.aop.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
        Object[] args = joinPoint.getArgs();

        String keyExpression = distributedLock.key();
        String lockKey = REDISSON_LOCK_PREFIX + parseKey(keyExpression, method, args);


        RLock couponLock = redissonClient.getLock(lockKey);

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
                } catch (IllegalMonitorStateException e) {
                    log.info(e.getMessage());
                }
            }
        }
    }

    private String parseKey(String keyExpression, Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            context.setVariable(parameters[i].getName(), args[i]);
        }

        Expression expression = parser.parseExpression(keyExpression);
        return expression.getValue(context, String.class);
    }
}


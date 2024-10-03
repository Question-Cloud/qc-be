package com.eager.questioncloud.aop;

import com.eager.questioncloud.annotation.DistributedLock;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Aspect
public class DistributedLockAspect {
    private static final String LOCK_PREFIX = "LOCK:";
    private final RedissonClient redissonClient;

    @Around("@annotation(com.eager.questioncloud.annotation.DistributedLock)")
    public Object lockProcess(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        DistributedLock distributedLock = signature.getMethod().getAnnotation(DistributedLock.class);
        String lockKey = LOCK_PREFIX + SpELParser.parse(signature.getParameterNames(), pjp.getArgs(), distributedLock.key());

        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS);
            if (!lock.isLocked()) {
                throw new CustomException(Error.INTERNAL_SERVER_ERROR);
            }
            return pjp.proceed();
        } catch (Exception e) {
            throw new CustomException(Error.INTERNAL_SERVER_ERROR);
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}

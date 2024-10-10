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
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
        boolean isWithTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        try {
            lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS);
            if (!lock.isLocked()) {
                throw new CustomException(Error.INTERNAL_SERVER_ERROR);
            }
            if (isWithTransaction) {
                registerUnlockAfterCommit(lock);
            }
            return pjp.proceed();
        } catch (Exception e) {
            if (lock.isLocked() && !isWithTransaction) {
                lock.unlock();
            }
            throw e;
        } finally {
            if (lock.isLocked() && !isWithTransaction) {
                lock.unlock();
            }
        }
    }

    private void registerUnlockAfterCommit(RLock lock) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }

            @Override
            public void afterCompletion(int status) {
                if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        });
    }
}

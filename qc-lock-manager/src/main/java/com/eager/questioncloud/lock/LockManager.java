package com.eager.questioncloud.lock;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

//TODO Exception throw 리팩토링

@Component
@RequiredArgsConstructor
public class LockManager {
    private static final String LOCK_PREFIX = "LOCK:";
    private final RedissonClient redissonClient;
    private final long WAIT_TIME = 3L;
    private final long LEASE_TIME = 5L;

    public void executeWithLock(String key, Runnable task) {
        String lockKey = LOCK_PREFIX + key;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!lock.isLocked()) {
                throw new RuntimeException();
            }
            task.run();
        } catch (InterruptedException e) {
            if (lock.isLocked()) {
                lock.unlock();
            }
            throw new RuntimeException();
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    public <T> T executeWithLock(String key, Callable<T> task) {
        String lockKey = LOCK_PREFIX + key;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!lock.isLocked()) {
                throw new RuntimeException();
            }
            return task.call();
        } catch (Exception e) {
            if (lock.isLocked()) {
                lock.unlock();
            }
            throw new RuntimeException();
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}

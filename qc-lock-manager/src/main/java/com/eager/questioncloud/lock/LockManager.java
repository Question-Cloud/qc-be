package com.eager.questioncloud.lock;

import com.eager.questioncloud.lock.exception.LockException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

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
            if (!lock.isLocked() && lock.isHeldByCurrentThread()) {
                throw new LockException();
            }
            task.run();
        } catch (Exception e) {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            throw new LockException();
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public <T> T executeWithLock(String key, Callable<T> task) {
        String lockKey = LOCK_PREFIX + key;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!lock.isLocked() && lock.isHeldByCurrentThread()) {
                throw new LockException();
            }
            return task.call();
        } catch (LockException e) {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            throw e;
        } catch (Exception e) {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            throw new LockException();
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

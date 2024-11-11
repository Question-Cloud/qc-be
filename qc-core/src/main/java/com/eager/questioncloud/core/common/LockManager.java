package com.eager.questioncloud.core.common;

import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
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
            if (!lock.isLocked()) {
                throw new CustomException(Error.INTERNAL_SERVER_ERROR);
            }
            task.run();
        } catch (InterruptedException e) {
            if (lock.isLocked()) {
                lock.unlock();
            }
            throw new CustomException(Error.INTERNAL_SERVER_ERROR);
        } catch (CustomException e) {
            if (lock.isLocked()) {
                lock.unlock();
            }
            throw e;
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}

package com.eager.questioncloud.lock

import com.eager.questioncloud.lock.exception.LockException
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class LockManager(private val redissonClient: RedissonClient) {
    private val WAIT_TIME = 3L
    private val LEASE_TIME = 5L
    
    fun executeWithLock(key: String, task: Runnable) {
        val lockKey = LOCK_PREFIX + key
        val lock = redissonClient.getLock(lockKey)
        
        runCatching {
            lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS)
            if (!lock.isLocked) {
                throw LockException()
            }
            task.run()
        }.also {
            if (lock.isLocked && lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }.getOrThrow()
    }
    
    companion object {
        private const val LOCK_PREFIX = "LOCK:"
    }
}

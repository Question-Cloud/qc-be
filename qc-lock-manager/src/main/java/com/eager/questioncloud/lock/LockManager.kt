package com.eager.questioncloud.lock

import com.eager.questioncloud.lock.exception.LockException
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

@Component
class LockManager(private val redissonClient: RedissonClient) {
    private val WAIT_TIME = 3L
    private val LEASE_TIME = 5L

    fun executeWithLock(key: String, task: Runnable) {
        val lockKey = LOCK_PREFIX + key
        val lock = redissonClient.getLock(lockKey)
        try {
            lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS)
            if (!lock.isLocked && lock.isHeldByCurrentThread) {
                throw LockException()
            }
            task.run()
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Exception) {
            throw LockException()
        } finally {
            if (lock.isLocked && lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }

    fun <T> executeWithLock(key: String, task: Callable<T>): T {
        val lockKey = LOCK_PREFIX + key
        val lock = redissonClient.getLock(lockKey)
        try {
            lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS)
            if (!lock.isLocked && lock.isHeldByCurrentThread) {
                throw LockException()
            }
            return task.call()
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Exception) {
            throw LockException()
        } finally {
            if (lock.isLocked && lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }

    companion object {
        private const val LOCK_PREFIX = "LOCK:"
    }
}

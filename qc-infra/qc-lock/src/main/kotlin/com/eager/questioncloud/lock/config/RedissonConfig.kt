package com.eager.questioncloud.lock.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig {
    @Value("\${REDIS_HOST}")
    private lateinit var REDIS_HOST: String
    
    @Value("\${REDIS_PORT}")
    private var REDIS_PORT = 0
    
    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer().setAddress("$REDISSON_HOST_PREFIX$REDIS_HOST:$REDIS_PORT")
        return Redisson.create(config)
    }
    
    companion object {
        private const val REDISSON_HOST_PREFIX = "redis://"
    }
}

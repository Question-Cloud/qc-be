package com.eager.questioncloud.application.config

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FailChargePointQueueConfig {
    @Bean
    fun failChargePointQueue(): Queue {
        return QueueBuilder.durable(FAIL_CHARGE_POINT_QUEUE)
            .build()
    }

    @Bean
    fun bindingFailChargePoint(failChargePointQueue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(failChargePointQueue).to(exchange).with(FAIL_CHARGE_POINT_QUEUE)
    }
    
    companion object {
        private const val FAIL_CHARGE_POINT_QUEUE = "fail.charge.point"

    }
}
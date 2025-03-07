package com.eager.questioncloud.application.config

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DelayQueueConfig {
    @Bean
    fun delayQueue(): Queue {
        return QueueBuilder.durable(DELAY_QUEUE)
            .deadLetterExchange("")
            .build()
    }

    @Bean
    fun delayQueueFanoutExchange(): FanoutExchange {
        return FanoutExchange(DELAY_EXCHANGE)
    }

    @Bean
    fun bindingDelay(delayQueue: Queue, delayQueueFanoutExchange: FanoutExchange): Binding {
        return BindingBuilder.bind(delayQueue).to(delayQueueFanoutExchange)
    }

    companion object {
        private const val DELAY_QUEUE = "delay"
        const val DELAY_EXCHANGE: String = "qc.delay.exchange"
    }
}
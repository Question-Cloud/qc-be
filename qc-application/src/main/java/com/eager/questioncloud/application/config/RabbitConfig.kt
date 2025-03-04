package com.eager.questioncloud.application.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {
    @Bean
    fun failChargePointQueue(): Queue {
        return QueueBuilder.durable(FAIL_CHARGE_POINT_QUEUE)
            .build()
    }

    @Bean
    fun failQuestionPaymentQueue(): Queue {
        return QueueBuilder.durable(FAIL_QUESTION_PAYMENT_QUEUE)
            .build()
    }

    @Bean
    fun delayQueue(): Queue {
        return QueueBuilder.durable(DELAY_QUEUE)
            .deadLetterExchange(EXCHANGE)
            .build()
    }

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange(EXCHANGE)
    }

    @Bean
    fun delayQueueFanoutExchange(): FanoutExchange {
        return FanoutExchange(DELAY_EXCHANGE)
    }

    @Bean
    fun bindingFailChargePoint(failChargePointQueue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(failChargePointQueue).to(exchange).with(FAIL_CHARGE_POINT_QUEUE)
    }

    @Bean
    fun bindingFailQuestionPayment(failQuestionPaymentQueue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(failQuestionPaymentQueue).to(exchange).with(FAIL_QUESTION_PAYMENT_QUEUE)
    }

    @Bean
    fun bindingDelay(delayQueue: Queue, delayQueueFanoutExchange: FanoutExchange): Binding {
        return BindingBuilder.bind(delayQueue).to(delayQueueFanoutExchange)
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jackson2JsonMessageConverter()
        return rabbitTemplate
    }

    @Bean
    fun jackson2JsonMessageConverter(): MessageConverter {
        val objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return Jackson2JsonMessageConverter(objectMapper)
    }

    companion object {
        private const val FAIL_CHARGE_POINT_QUEUE = "fail-charge-point"
        private const val FAIL_QUESTION_PAYMENT_QUEUE = "fail-question-payment"
        private const val DELAY_QUEUE = "delay"

        private const val EXCHANGE = "qc.exchange"
        const val DELAY_EXCHANGE: String = "qc.delay.exchange"
    }
}

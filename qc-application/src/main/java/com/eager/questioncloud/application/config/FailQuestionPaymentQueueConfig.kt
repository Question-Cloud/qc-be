package com.eager.questioncloud.application.config

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FailQuestionPaymentQueueConfig {
    @Bean
    fun failQuestionPaymentQueue(): Queue {
        return QueueBuilder.durable(FAIL_QUESTION_PAYMENT_QUEUE)
            .build()
    }

    @Bean
    fun bindingFailQuestionPayment(failQuestionPaymentQueue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(failQuestionPaymentQueue).to(exchange).with(FAIL_QUESTION_PAYMENT_QUEUE)
    }

    companion object {
        private const val FAIL_QUESTION_PAYMENT_QUEUE = "fail.question.payment"
    }
}
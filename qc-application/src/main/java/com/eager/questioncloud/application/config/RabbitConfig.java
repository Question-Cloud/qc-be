package com.eager.questioncloud.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    private static final String FAIL_CHARGE_POINT_QUEUE = "fail-charge-point";
    private static final String FAIL_QUESTION_PAYMENT_QUEUE = "fail-question-payment";
    private static final String DELAY_QUEUE = "delay";

    private static final String EXCHANGE = "qc.exchange";
    public static final String DELAY_EXCHANGE = "qc.delay.exchange";

    @Bean
    public Queue failChargePointQueue() {
        return QueueBuilder.durable(FAIL_CHARGE_POINT_QUEUE)
            .build();
    }

    @Bean
    public Queue failQuestionPaymentQueue() {
        return QueueBuilder.durable(FAIL_QUESTION_PAYMENT_QUEUE)
            .build();
    }

    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(DELAY_QUEUE)
            .deadLetterExchange(EXCHANGE)
            .build();
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public FanoutExchange delayQueueFanoutExchange() {
        return new FanoutExchange(DELAY_EXCHANGE);
    }

    @Bean
    public Binding bindingFailChargePoint(Queue failChargePointQueue, TopicExchange exchange) {
        return BindingBuilder.bind(failChargePointQueue).to(exchange).with(FAIL_CHARGE_POINT_QUEUE);
    }

    @Bean
    public Binding bindingFailQuestionPayment(Queue failQuestionPaymentQueue, TopicExchange exchange) {
        return BindingBuilder.bind(failQuestionPaymentQueue).to(exchange).with(FAIL_QUESTION_PAYMENT_QUEUE);
    }

    @Bean
    public Binding bindingDelay(Queue delayQueue, FanoutExchange delayQueueFanoutExchange) {
        return BindingBuilder.bind(delayQueue).to(delayQueueFanoutExchange);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}

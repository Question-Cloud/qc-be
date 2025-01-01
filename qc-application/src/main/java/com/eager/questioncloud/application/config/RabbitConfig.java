package com.eager.questioncloud.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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
    private static final String FAIL_CHARGE_POINT_DEAD_LETTER_QUEUE = "fail-charge-point-dead-letter";
    private static final String FAIL_QUESTION_PAYMENT_QUEUE = "fail-question-payment";
    private static final String FAIL_QUESTION_PAYMENT_DEAD_LETTER_QUEUE = "fail-question-payment-dead-letter";
    private static final String EXCHANGE = "qc.exchange";
    private static final String DEAD_LETTER_EXCHANGE = "qc.deadLetterExchange";

    @Bean
    public Queue failChargePointQueue() {
        return QueueBuilder.durable(FAIL_CHARGE_POINT_QUEUE)
            .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", FAIL_CHARGE_POINT_DEAD_LETTER_QUEUE)
            .build();
    }

    @Bean
    public Queue failChargePointDeadLetterQueue() {
        return new Queue(FAIL_CHARGE_POINT_DEAD_LETTER_QUEUE);
    }

    @Bean
    public Queue failQuestionPaymentQueue() {
        return QueueBuilder.durable(FAIL_QUESTION_PAYMENT_QUEUE)
            .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", FAIL_QUESTION_PAYMENT_DEAD_LETTER_QUEUE)
            .build();
    }

    @Bean
    public Queue failQuestionPaymentDeadLetterQueue() {
        return new Queue(FAIL_QUESTION_PAYMENT_DEAD_LETTER_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    public Binding bindingFailChargePoint(Queue failChargePointQueue, TopicExchange exchange) {
        return BindingBuilder.bind(failChargePointQueue).to(exchange).with(FAIL_CHARGE_POINT_QUEUE);
    }

    @Bean
    public Binding bindingFailChargePointDeadLetter(Queue failChargePointDeadLetterQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder.bind(failChargePointDeadLetterQueue).to(deadLetterExchange).with(FAIL_CHARGE_POINT_DEAD_LETTER_QUEUE);
    }

    @Bean
    public Binding bindingFailQuestionPayment(Queue failQuestionPaymentQueue, TopicExchange exchange) {
        return BindingBuilder.bind(failQuestionPaymentQueue).to(exchange).with(FAIL_QUESTION_PAYMENT_QUEUE);
    }

    @Bean
    public Binding bindingFailQuestionPaymentDeadLetter(Queue failQuestionPaymentDeadLetterQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder.bind(failQuestionPaymentDeadLetterQueue).to(deadLetterExchange).with(FAIL_QUESTION_PAYMENT_DEAD_LETTER_QUEUE);
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

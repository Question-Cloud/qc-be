package com.eager.questioncloud.application.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Queue failChargePointQueue() {
        return new Queue("fail-charge-point");
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("qc.exchange");
    }

    @Bean
    public Binding bindingFailChargePoint(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("fail-charge-point");
    }
}

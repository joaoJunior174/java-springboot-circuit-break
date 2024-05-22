package com.example.ratings.orderexporter.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbit.ratings.order.queue.name}")
    private String queueName;

    @Value("${rabbit.ratings.order.routingkey.name}")
    private String routingKey;

    @Value("${rabbit.ratings.order.exchange.name}")
    private String exchangeName;

    @Value("${rabbit.ratings.order.dlq.queue.name}")
    private String dlqQueueName;

    @Value("${rabbit.ratings.order.dlq.routingkey.name}")
    private String dlqRoutingKey;

    @Value("${rabbit.ratings.order.dlq.exchange.name}")
    private String dlqExchangeName;

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }
    @Bean
    Queue queue() {
        return QueueBuilder
                .durable(queueName)
                .withArgument("x-dead-letter-exchange", exchangeName)
                .withArgument("x-dead-letter-routing-key", dlqQueueName)
                .build();
    }
    @Bean
    Queue dlqQueue() {
        return QueueBuilder
                .durable(dlqQueueName)
                .withArgument("x-dead-letter-exchange", exchangeName)
                .withArgument("x-dead-letter-routing-key", routingKey)
                .ttl(100)
                .build();
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder
                .bind(dlqQueue())
                .to(exchange())
                .with(dlqQueueName);
    }


    @Bean
    Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }
}
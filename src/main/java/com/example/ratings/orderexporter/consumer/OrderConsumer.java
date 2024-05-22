package com.example.ratings.orderexporter.consumer;

import com.example.ratings.orderexporter.OrderDto;
import com.example.ratings.orderexporter.OrderExporterApplication;
import com.example.ratings.orderexporter.feign.OrderFeign;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.rmi.ServerException;

@Service
public class OrderConsumer {

    @Autowired
    private OrderFeign orderFeign;

    Logger logger = LoggerFactory.getLogger(OrderExporterApplication.class);

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbit.ratings.order.exchange.name}")
    private String exchange;
    @Value("${rabbit.ratings.order.dlq.queue.name}")
    private String routingKey;


    public OrderConsumer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

   @RabbitListener(queues = {"${rabbitmq.queue.name}"}, concurrency = "${rabbitmq.queue.consumer.quantity}")
    public void consume(Message message)  {
        try {
            orderFeign.sendOrder(convertMessageToDto(message));
        } catch (Exception e) {
            logger.error(e.getMessage());
            this.rabbitTemplate.convertAndSend(exchange, routingKey, message.getBody());
        }
    }

    @RabbitListener(queues = {"${rabbit.ratings.order.queue.name}"}, concurrency = "${rabbitmq.queue.consumer.quantity}")
    public void consumeFromRetry(Message message) throws Exception {
        try {
            orderFeign.sendOrder(convertMessageToDto(message));
            logger.error("Deu Bom na fila de retentativa!");
        } catch (Exception e) {
            logger.error("Deu ruim na fila de retentativa!");
        }
    }

    private OrderDto convertMessageToDto(Message message) throws Exception {
        String messageBody = new String(message.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(messageBody, new TypeReference<OrderDto>() {});
    }
}

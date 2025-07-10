package com.petverse.producer;

import com.petverse.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivityEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
        System.out.println("Event gönderildi: " + message);
    }
}

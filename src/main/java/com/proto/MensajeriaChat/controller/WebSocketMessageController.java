package com.proto.MensajeriaChat.controller;

import com.proto.MensajeriaChat.config.RabbitConfig;
import com.proto.MensajeriaChat.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketMessageController {
    private final RabbitTemplate rabbitTemplate;

    @MessageMapping("/rooms/{roomId}/sendMessage")
    public void sendMessage(ChatMessageDTO msg) {
        // publish to rabbit exchange with routing key room.<roomId>
        String routingKey = "room." + msg.getRoomId();
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, routingKey, msg);
    }

}

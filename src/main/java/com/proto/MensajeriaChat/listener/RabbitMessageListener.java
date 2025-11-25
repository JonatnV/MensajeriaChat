package com.proto.MensajeriaChat.listener;

import com.proto.MensajeriaChat.dto.ChatMessageDTO;
import com.proto.MensajeriaChat.model.Message;
import com.proto.MensajeriaChat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RabbitMessageListener {
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simp;

    @RabbitListener(queues = "chat.general")
    public void onMessage(ChatMessageDTO dto) {
        // persist
        Message m = new Message();
        m.setRoomId(dto.getRoomId());
        m.setSenderId(dto.getSenderId());
        m.setSenderName(dto.getSenderName());
        m.setContent(dto.getContent());
        m.setCreatedAt(Instant.now());
        messageRepository.save(m);

        // re-broadcast to WebSocket clients subscribed to /topic/rooms/{roomId}
        simp.convertAndSend("/topic/rooms/" + dto.getRoomId(), dto);
    }
}
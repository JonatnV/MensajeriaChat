package com.proto.MensajeriaChat.service;



import com.proto.MensajeriaChat.config.RabbitConfig;
import com.proto.MensajeriaChat.dto.ChatMessageDTO;
import com.proto.MensajeriaChat.model.*;
import com.proto.MensajeriaChat.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MessageRepository messageRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Message sendMessage(ChatMessageDTO dto) {
        Long userId = dto.getSenderId();
        Long roomId = dto.getRoomId();

        User u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room r = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Optional<RoomMember> membership = roomMemberRepository.findByRoom_IdAndUser_Id(roomId, userId);

        if (r.isPrivate()) {
            if (membership.isEmpty() || !membership.get().isActive()) {
                throw new RuntimeException("User doesn't have access to private room");
            }
        }

        Message m = new Message();
        m.setRoomId(roomId);
        m.setSenderId(userId);
        m.setSenderName(u.getDisplayName() != null ? u.getDisplayName() : u.getUsername());
        m.setContent(dto.getContent());
        m.setCreatedAt(Instant.now());

        messageRepository.save(m);

        // publicar DTO al exchange con routing key "room.{roomId}"
        String routingKey = "room." + roomId;
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, routingKey, dto);

        return m;
    }

    @Transactional
    public void joinRoom(Long userId, Long roomId) {
        User u = userRepository.findById(userId).orElseThrow();
        Room r = roomRepository.findById(roomId).orElseThrow();

        RoomMember rm = roomMemberRepository.findByRoom_IdAndUser_Id(roomId, userId)
                .orElse(RoomMember.builder().room(r).user(u).active(true).build());
        rm.setActive(true);
        roomMemberRepository.save(rm);

        // enviar evento de join como mensaje de tipo JOIN por el exchange (opcional)
        ChatMessageDTO evt = new ChatMessageDTO();
        evt.setRoomId(roomId);
        evt.setSenderId(userId);
        evt.setSenderName(u.getDisplayName());
        evt.setContent(userId + " joined");
        evt.setType("JOIN");
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "room." + roomId, evt);
    }

    @Transactional
    public void leaveRoom(Long userId, Long roomId) {
        RoomMember rm = roomMemberRepository.findByRoom_IdAndUser_Id(roomId, userId)
                .orElseThrow(() -> new RuntimeException("Not member"));
        rm.setActive(false);
        roomMemberRepository.save(rm);

        ChatMessageDTO evt = new ChatMessageDTO();
        evt.setRoomId(roomId);
        evt.setSenderId(userId);
        evt.setSenderName(rm.getUser().getDisplayName());
        evt.setContent(userId + " left");
        evt.setType("LEAVE");
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "room." + roomId, evt);
    }
}
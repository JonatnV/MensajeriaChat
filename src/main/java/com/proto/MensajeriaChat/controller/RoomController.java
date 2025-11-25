package com.proto.MensajeriaChat.controller;

import com.proto.MensajeriaChat.dto.RoomCreationDTO;
import com.proto.MensajeriaChat.model.Message;
import com.proto.MensajeriaChat.model.Room;
import com.proto.MensajeriaChat.repository.MessageRepository;
import com.proto.MensajeriaChat.repository.RoomRepository;
import com.proto.MensajeriaChat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody RoomCreationDTO dto) {
        Room r = new Room();
        r.setName(dto.getName());
        r.setPrivate(dto.isPrivate());
        r.setPassword(dto.getPassword()); // en PoC se guarda así; en prod hashear
        Room saved = roomRepository.save(r);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{roomId}/messages")
    public Page<Message> history(@PathVariable Long roomId,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size) {
        return messageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, PageRequest.of(page, size));
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<String> join(@PathVariable Long roomId, Authentication auth) {
        Long uid = (Long) auth.getPrincipal();
        chatService.joinRoom(uid, roomId);
        return ResponseEntity.ok("joined");
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<String> leave(@PathVariable Long roomId, Authentication auth) {
        Long uid = (Long) auth.getPrincipal();
        chatService.leaveRoom(uid, roomId);
        return ResponseEntity.ok("left");
    }
}
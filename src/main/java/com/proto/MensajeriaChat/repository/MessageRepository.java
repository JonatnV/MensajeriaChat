package com.proto.MensajeriaChat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.proto.MensajeriaChat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {
    Page<Message> findByRoomIdOrderByCreatedAtDesc(Long roomId,Pageable pageable);
}

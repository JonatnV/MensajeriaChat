package com.proto.MensajeriaChat.repository;

import com.proto.MensajeriaChat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}

package com.proto.MensajeriaChat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity @Table(name = "messages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long roomId;
    private Long senderId;
    private String senderName;
    @Column(columnDefinition = "text")
    private String content;
    private Instant createdAt;
}

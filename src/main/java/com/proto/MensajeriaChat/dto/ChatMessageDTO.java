package com.proto.MensajeriaChat.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChatMessageDTO {
    private Long roomId;
    private String content;
    private Long senderId;
    private String senderName;
    private String type;
}

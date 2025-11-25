package com.proto.MensajeriaChat.dto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomCreationDTO {
    private String name;
    private boolean isPrivate;
    private String password;
}

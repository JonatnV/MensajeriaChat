package com.proto.MensajeriaChat.model;
import lombok.*;
import jakarta.persistence.*;
@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true,nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String displayName;

}

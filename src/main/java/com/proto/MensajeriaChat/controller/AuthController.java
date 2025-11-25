package com.proto.MensajeriaChat.controller;

import com.proto.MensajeriaChat.config.JwtUtil;
import com.proto.MensajeriaChat.dto.AuthRequest;
import com.proto.MensajeriaChat.dto.AuthResponse;
import com.proto.MensajeriaChat.model.User;
import com.proto.MensajeriaChat.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest req) {
        var u = authService.register(req.getUsername(), req.getPassword(), req.getUsername());
        String token = jwtUtil.generateToken(u.getUsername(), u.getId());
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        User u = authService.authenticate(req.getUsername(), req.getPassword());
        if (u == null) throw new RuntimeException("Invalid credentials");
        String token = jwtUtil.generateToken(u.getUsername(), u.getId());
        return new AuthResponse(token);
    }
}
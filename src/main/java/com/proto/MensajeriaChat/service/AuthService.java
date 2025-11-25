package com.proto.MensajeriaChat.service;

import com.proto.MensajeriaChat.model.User;
import com.proto.MensajeriaChat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User register(String username, String password, String displayName) {
        var u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setDisplayName(displayName == null ? username : displayName);
        return userRepository.save(u);
    }

    public User authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> encoder.matches(password, u.getPassword()))
                .orElse(null);
    }
}
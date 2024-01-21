package com.example.subscription.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Sahand Jalilvand 21.01.24
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EncoderService {

    private final PasswordEncoder passwordEncoder;

    @Value("${subscription.security.hash-salt}")
    private String hashSalt;

    public String encode(String username, String password) {
        var saltedPassword = getSaltedPassword(username, password);
        return passwordEncoder.encode(saltedPassword);
    }

    public boolean matchPassword(String username, String rowPassword, String encodedPassword) {
        var saltedPassword = getSaltedPassword(username, rowPassword);
        return passwordEncoder.matches(saltedPassword, encodedPassword);
    }

    private String getSaltedPassword(String username, String password) {
        return hashSalt + username + password;
    }
}

package com.example.subscription.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@RequiredArgsConstructor
public enum UserAuthorityEnum {

    ADMIN(true),
    NORMAL(true);

    private final boolean isRole;

    private String getAuthority() {
        return isRole ? "ROLE_".concat(name()) : name();
    }

    public static List<SimpleGrantedAuthority> convertToGrantedAuthority(List<UserAuthorityEnum> authorities) {

        if (authorities == null) {
            return Collections.emptyList();
        }

        return authorities.stream()
                .map(UserAuthorityEnum::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}

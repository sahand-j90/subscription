package com.example.subscription.services;

import com.example.subscription.security.SecurityFacade;
import com.example.subscription.services.dto.LoginDto;
import com.example.subscription.services.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final SecurityFacade securityFacade;

    public TokenDto login(LoginDto login) {

        var username = login.getUsername();
        var password = login.getPassword();

        var token = securityFacade.credentialsLogin(username, password);

        return new TokenDto(token);
    }
}

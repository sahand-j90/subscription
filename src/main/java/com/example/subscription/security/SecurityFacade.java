package com.example.subscription.security;

import com.example.subscription.enums.AuthenticationFlowEnum;
import com.example.subscription.security.authentication.CredentialsAuthenticationToken;
import com.example.subscription.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityFacade {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public String credentialsLogin(String username, String password) {

        var authentication = new CredentialsAuthenticationToken(username, password);
        authenticationManager.authenticate(authentication);

        return jwtTokenService.generateToken(AuthenticationFlowEnum.CREDENTIALS, username, new HashMap<>());
    }

}
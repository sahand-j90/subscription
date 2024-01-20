package com.example.subscription.security;

import com.example.subscription.common.DistributedLock;
import com.example.subscription.enums.AuthenticationFlowEnum;
import com.example.subscription.exceptions.BizException;
import com.example.subscription.exceptions.Errors;
import com.example.subscription.security.authentication.CredentialsAuthenticationToken;
import com.example.subscription.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RateIntervalUnit;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityFacade {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final DistributedLock distributedLock;

    public String credentialsLogin(String username, String password) {

        if (!distributedLock.rateLimit(username, 3, 1, RateIntervalUnit.MINUTES)) {
            throw new BizException(Errors.TOO_MANY_REQUEST_EXCEPTION);
        }

        var authentication = new CredentialsAuthenticationToken(username, password);

        var authenticated = authenticationManager.authenticate(authentication);
        var authorities = authenticated.getAuthorities();

        return jwtTokenService.generateToken(AuthenticationFlowEnum.CREDENTIALS, username, authorities);
    }

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matchPassword(String rowPassword, String encodedPassword) {
        return passwordEncoder.matches(rowPassword, encodedPassword);
    }

}

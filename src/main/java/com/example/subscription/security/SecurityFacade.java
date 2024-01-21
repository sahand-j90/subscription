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
    private final EncoderService encoderService;
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

    public String encode(String username, String password) {
        return encoderService.encode(username, password);
    }

    public boolean matchPassword(String username, String rowPassword, String encodedPassword) {
        return encoderService.matchPassword(username, rowPassword, encodedPassword);
    }

}

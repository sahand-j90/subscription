package com.example.subscription.security.authorization;

import com.example.subscription.enums.AuthenticationFlowEnum;
import com.example.subscription.security.authentication.CredentialsAuthenticationToken;
import com.example.subscription.security.userdetail.CredentialsUserDetailsService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CredentialsAuthorizationCommand extends AuthorizationCommandTemplate {

    private final CredentialsUserDetailsService credentialsUserDetailsService;

    @Override
    public AuthenticationFlowEnum authenticationFlow() {
        return AuthenticationFlowEnum.CREDENTIALS;
    }

    @Override
    protected AbstractAuthenticationToken provideAuthentication(Claims claims) {
        // TODO: 20.01.24 load from JWT 
        var details = credentialsUserDetailsService.loadUserByUsername(claims.getSubject());
        return new CredentialsAuthenticationToken(details.getUsername(), null, details.getAuthorities());
    }
}

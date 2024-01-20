package com.example.subscription.security.authentication;

import com.example.subscription.exceptions.BizException;
import com.example.subscription.exceptions.Errors;
import com.example.subscription.security.userdetail.CredentialsUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Component
@RequiredArgsConstructor
public class CredentialsAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final CredentialsUserDetailsService credentialsUserDetailsService;

    @Override
    public CredentialsAuthenticationToken authenticate(Authentication authentication) throws AuthenticationException {

        var generalUsernamePasswordAuthentication = (CredentialsAuthenticationToken) authentication;

        var username = generalUsernamePasswordAuthentication.getName();
        var password = generalUsernamePasswordAuthentication.getCredentials().toString();

        var userDetails = credentialsUserDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BizException(Errors.INVALID_CREDENTIALS_EXCEPTION);
        }

        if (!userDetails.isEnabled()) {
            throw new BizException(Errors.USER_IS_NOT_ACTIVE_EXCEPTION);
        }

        return new CredentialsAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CredentialsAuthenticationToken.class.isAssignableFrom(aClass);
    }
}

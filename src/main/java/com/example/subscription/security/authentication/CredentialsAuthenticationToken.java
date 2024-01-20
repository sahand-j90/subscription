package com.example.subscription.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Sahand Jalilvand 20.01.24
 */
public class CredentialsAuthenticationToken extends UsernamePasswordAuthenticationToken {


    public CredentialsAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public CredentialsAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}

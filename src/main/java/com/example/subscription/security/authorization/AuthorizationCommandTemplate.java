package com.example.subscription.security.authorization;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Date;

/**
 * @author Sahand Jalilvand 20.01.24
 */
public abstract class AuthorizationCommandTemplate implements AuthorizationCommand {

    @Override
    public final void execute(Claims claims, HttpServletRequest request, HttpServletResponse response) {
        if (isNotExpired(claims)) {
            var authentication = provideAuthentication(claims);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    protected abstract AbstractAuthenticationToken provideAuthentication(Claims claims);

    private boolean isNotExpired(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

}

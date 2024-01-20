package com.example.subscription.security.filters;

import com.example.subscription.enums.AuthenticationFlowEnum;
import com.example.subscription.security.authorization.AuthorizationCommandFactory;
import com.example.subscription.security.jwt.JwtTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final AuthorizationCommandFactory authorizationCommandFactory;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            setUserAuthentication(jwt, request, response);
        }

        chain.doFilter(request, response);
    }

    private void setUserAuthentication(String token, HttpServletRequest request, HttpServletResponse response) {

        Claims claims = jwtTokenService.extractAllClaims(token);

        AuthenticationFlowEnum authenticationFlow = AuthenticationFlowEnum.valueOf(claims.getIssuer());

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            authorizationCommandFactory.getCommand(authenticationFlow)
                    .ifPresent(authorizationCommand -> authorizationCommand.execute(claims, request, response));
        }
    }

}

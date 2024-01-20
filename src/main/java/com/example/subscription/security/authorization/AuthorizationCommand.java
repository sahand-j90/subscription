package com.example.subscription.security.authorization;

import com.example.subscription.enums.AuthenticationFlowEnum;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Sahand Jalilvand 20.01.24
 */
public interface AuthorizationCommand {

    AuthenticationFlowEnum authenticationFlow();

    void execute(Claims claims, HttpServletRequest request, HttpServletResponse response);

}

package com.example.subscription.security.conf;

import com.example.subscription.exceptions.Errors;
import com.example.subscription.exceptions.models.ErrorMessage;
import com.example.subscription.i18n.MessageResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@RequiredArgsConstructor
public class GeneralAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final MessageResolver messageResolver;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        initHeaders(httpServletResponse);
        fillBody(httpServletResponse);
    }


    private void initHeaders(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
    }

    private void fillBody(HttpServletResponse response) throws IOException {
        var errorBundle = Errors.INVALID_CREDENTIALS_EXCEPTION;
        var error = ErrorMessage.builder()
                .code(errorBundle.code())
                .description(messageResolver.getMessage(errorBundle.description()))
                .build();

        response.getWriter().write(objectMapper.valueToTree(error).toString());
    }
}

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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@RequiredArgsConstructor
public class GeneralAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final MessageResolver messageResolver;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        initHeaders(httpServletResponse);
        fillBody(httpServletResponse);
    }

    private void initHeaders(HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");
    }

    private void fillBody(HttpServletResponse response) throws IOException {

        var errorBundle = Errors.ACCESS_DENIED_EXCEPTION;
        var error = ErrorMessage.builder()
                .code(errorBundle.code())
                .description(messageResolver.getMessage(errorBundle.description()))
                .build();

        response.getWriter().write(objectMapper.valueToTree(error).toString());
    }

}

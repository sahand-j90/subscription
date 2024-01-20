package com.example.subscription.exceptions;

import com.example.subscription.common.TracerService;
import com.example.subscription.exceptions.models.ErrorMessage;
import com.example.subscription.exceptions.models.ValidationItem;
import com.example.subscription.i18n.MessageResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Sahand Jalilvand 19.01.24
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class RestExceptionAdvice {

    private final MessageResolver messageResolver;
    private final TracerService tracerService;

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorMessage> handle(BizException ex, HttpServletRequest request) {
        log.error("ErrorCode [{}], Url [{}]", ex.getErrorBundle().code(), request.getRequestURI(), ex);

        var errorBundle = ex.getErrorBundle();
        var error = ErrorMessage.builder()
                .code(errorBundle.code())
                .description(messageResolver.getMessage(errorBundle.description()))
                .spanId(getCurrentSpanId())
                .build();

        return ResponseEntity.status(errorBundle.httpStatus())
                .body(error);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorMessage> handle(BindException ex, HttpServletRequest request) {
        log.error("BindException Url [{}]", request.getRequestURI(), ex);

        Function<FieldError, ValidationItem> mapper = fieldError -> {
            var message = Optional.ofNullable(messageResolver.getMessage(fieldError.getDefaultMessage()))
                    .orElse(fieldError.getDefaultMessage());
            return new ValidationItem(fieldError.getField(), message);
        };

        var validationErrorItems = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());

        var errorBundle = Errors.VALIDATION_EXCEPTION;
        var error = ErrorMessage.builder()
                .code(errorBundle.code())
                .description(messageResolver.getMessage(errorBundle.description()))
                .spanId(getCurrentSpanId())
                .validationErrorItems(validationErrorItems)
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorMessage> handle(ObjectOptimisticLockingFailureException ex, HttpServletRequest request) {
        log.error("OptimisticLockException [{}]", request.getRequestURI(), ex);

        var errorBundle = Errors.OPTIMISTIC_LOCK_EXCEPTION;
        var error = ErrorMessage.builder()
                .code(errorBundle.code())
                .description(messageResolver.getMessage(errorBundle.description()))
                .spanId(getCurrentSpanId())
                .build();

        return ResponseEntity.status(errorBundle.httpStatus())
                .body(error);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handle(ConstraintViolationException ex, HttpServletRequest request) {
        log.error("ConstraintViolationException Url [{}]", request.getRequestURI(), ex);

        var message = Optional.ofNullable(ex.getConstraintViolations())
                .orElse(Collections.emptySet())
                .stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse(Errors.VALIDATION_EXCEPTION.description());

        var errorBundle = Errors.GENERAL_UNKNOWN_EXCEPTION;
        var error = ErrorMessage.builder()
                .code(errorBundle.code())
                .description(messageResolver.getMessage(message))
                .spanId(getCurrentSpanId())
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorMessage> handle(Throwable ex, HttpServletRequest request) {
        log.error("UnknownException [{}]", request.getRequestURI(), ex);

        var errorBundle = Errors.GENERAL_UNKNOWN_EXCEPTION;
        var error = ErrorMessage.builder()
                .code(errorBundle.code())
                .description(messageResolver.getMessage(errorBundle.description()))
                .spanId(getCurrentSpanId())
                .build();

        return ResponseEntity.status(errorBundle.httpStatus())
                .body(error);
    }

    private String getCurrentSpanId() {
        return Optional.ofNullable(tracerService.getSpanId())
                .orElse(null);
    }

}

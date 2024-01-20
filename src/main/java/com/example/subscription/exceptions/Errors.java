package com.example.subscription.exceptions;

import com.example.subscription.i18n.Messages;
import org.springframework.http.HttpStatus;

/**
 * @author Sahand Jalilvand 19.01.24
 */
public class Errors {

    // 400

    public static final ErrorBundle GENERAL_BIZ_EXCEPTION = new ErrorBundle(40000, HttpStatus.BAD_REQUEST, Messages.GENERAL_BIZ_EXCEPTION);

    public static final ErrorBundle VALIDATION_EXCEPTION = new ErrorBundle(40001, HttpStatus.BAD_REQUEST, Messages.VALIDATION_EXCEPTION);

    public static final ErrorBundle INVALID_SUBSCRIPTION_INTERVAL = new ErrorBundle(40002, HttpStatus.BAD_REQUEST, Messages.INVALID_SUBSCRIPTION_INTERVAL);

    public static final ErrorBundle SUBSCRIBER_ALREADY_EXISTS = new ErrorBundle(40003, HttpStatus.BAD_REQUEST, Messages.SUBSCRIBER_ALREADY_EXISTS);

    public static final ErrorBundle DELETE_NON_RESERVED_IS_NOT_POSSIBLE = new ErrorBundle(40004, HttpStatus.BAD_REQUEST, Messages.DELETE_NON_RESERVED_IS_NOT_POSSIBLE);

    public static final ErrorBundle SUBSCRIPTION_OVERLAP_EXCEPTION = new ErrorBundle(40005, HttpStatus.BAD_REQUEST, Messages.SUBSCRIPTION_OVERLAP_EXCEPTION);


    // 401
    public static final ErrorBundle INVALID_CREDENTIALS_EXCEPTION = new ErrorBundle(40100, HttpStatus.UNAUTHORIZED, Messages.INVALID_CREDENTIALS_EXCEPTION);
    public static final ErrorBundle USER_IS_NOT_ACTIVE_EXCEPTION = new ErrorBundle(40101, HttpStatus.UNAUTHORIZED, Messages.USER_IS_NOT_ACTIVE_EXCEPTION);

    // 403
    public static final ErrorBundle ACCESS_DENIED_EXCEPTION = new ErrorBundle(40300, HttpStatus.FORBIDDEN, Messages.ACCESS_DENIED_EXCEPTION);


    // 404

    public static final ErrorBundle SUBSCRIBER_NOT_FOUND = new ErrorBundle(40401, HttpStatus.NOT_FOUND, Messages.SUBSCRIBER_NOT_FOUND);

    public static final ErrorBundle SUBSCRIPTION_NOT_FOUND = new ErrorBundle(40402, HttpStatus.NOT_FOUND, Messages.SUBSCRIPTION_NOT_FOUND);

    // 500

    public static final ErrorBundle GENERAL_UNKNOWN_EXCEPTION = new ErrorBundle(50000, HttpStatus.INTERNAL_SERVER_ERROR, Messages.GENERAL_UNKNOWN_EXCEPTION);

    public static record ErrorBundle(int code, HttpStatus httpStatus, String description) {
    }
}

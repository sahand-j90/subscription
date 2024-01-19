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


    // 404

    public static final ErrorBundle SUBSCRIBER_NOT_FOUND = new ErrorBundle(40401, HttpStatus.NOT_FOUND, Messages.SUBSCRIBER_NOT_FOUND);

    public static final ErrorBundle SUBSCRIPTION_NOT_FOUND = new ErrorBundle(40402, HttpStatus.NOT_FOUND, Messages.SUBSCRIPTION_NOT_FOUND);

    // 500

    public static final ErrorBundle GENERAL_UNKNOWN_EXCEPTION = new ErrorBundle(50000, HttpStatus.INTERNAL_SERVER_ERROR, Messages.GENERAL_UNKNOWN_EXCEPTION);

    public static record ErrorBundle(int code, HttpStatus httpStatus, String description) {
    }
}

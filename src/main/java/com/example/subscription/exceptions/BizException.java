package com.example.subscription.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Sahand Jalilvand 19.01.24
 */
@RequiredArgsConstructor
@Getter
public class BizException extends RuntimeException {

    private final Errors.ErrorBundle errorBundle;

}

package com.example.subscription.services.validators;

import com.example.subscription.domains.SubscriptionEntity;
import com.example.subscription.exceptions.BizException;
import com.example.subscription.exceptions.Errors;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@RequiredArgsConstructor
public class SubscriptionDateValidation implements Validation {

    private final SubscriptionEntity subscription;

    @Override
    public void validate() {

        var from = subscription.getFrom();
        var to = subscription.getTo();

        if (from.isAfter(to)) {
            throw new BizException(Errors.INVALID_SUBSCRIPTION_INTERVAL);
        }

        if (from.isBefore(LocalDate.now())) {
            throw new BizException(Errors.INVALID_SUBSCRIPTION_INTERVAL);
        }
    }
}

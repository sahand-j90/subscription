package com.example.subscription.services.validators;

import com.example.subscription.exceptions.BizException;
import com.example.subscription.exceptions.Errors;
import com.example.subscription.repositories.SubscriberRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@RequiredArgsConstructor
public class SubscriberNameAlreadyExistsValidation implements Validation {

    private final String subscriberName;
    private final SubscriberRepository subscriberRepository;

    @Override
    public void validate() {
        var subscriber = subscriberRepository.findBySubscriberName(subscriberName);

        if (subscriber.isPresent()) {
            throw new BizException(Errors.SUBSCRIBER_ALREADY_EXISTS);
        }
    }
}

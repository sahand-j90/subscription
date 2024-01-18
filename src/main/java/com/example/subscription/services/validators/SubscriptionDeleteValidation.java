package com.example.subscription.services.validators;

import com.example.subscription.enums.SubscriptionStateEnum;
import com.example.subscription.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * @author Sahand Jalilvand 19.01.24
 */
@RequiredArgsConstructor
public class SubscriptionDeleteValidation implements Validation {

    private final UUID id;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void validate() {
        var subscription = subscriptionRepository.findById(id)
                .orElseThrow();

        if (SubscriptionStateEnum.RESERVED != subscription.getState()) {
            throw new RuntimeException();
        }
    }
}

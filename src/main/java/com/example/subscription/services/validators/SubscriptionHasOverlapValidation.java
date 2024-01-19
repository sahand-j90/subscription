package com.example.subscription.services.validators;

import com.example.subscription.domains.SubscriptionEntity;
import com.example.subscription.exceptions.BizException;
import com.example.subscription.exceptions.Errors;
import com.example.subscription.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@RequiredArgsConstructor
public class SubscriptionHasOverlapValidation implements Validation {


    private final SubscriptionEntity subscription;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void validate() {

        var userId = subscription.getSubscriber().getId();
        var from = subscription.getFrom();
        var to = subscription.getTo();

        var overlaps = subscriptionRepository.findAllBetweenDates(userId, from, to);

        if (!overlaps.isEmpty()) {
            throw new BizException(Errors.SUBSCRIPTION_OVERLAP_EXCEPTION);
        }
    }
}

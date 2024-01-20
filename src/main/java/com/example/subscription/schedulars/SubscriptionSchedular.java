package com.example.subscription.schedulars;

import com.example.subscription.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Sahand Jalilvand 21.01.24
 */
@Component
@RequiredArgsConstructor
public class SubscriptionSchedular {

    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "${subscription.scheduling.subscription-batch-cron}")
    public void subscriptionBatchProcessing() {
        subscriptionService.subscriptionBatchProcessing();
    }

}

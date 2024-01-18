package com.example.subscription.services;

import com.example.subscription.repositories.SubscriptionRepository;
import com.example.subscription.services.dto.SubscriptionDto;
import com.example.subscription.services.mapper.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {


    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Transactional(readOnly = true)
    public SubscriptionDto get(String id) {

        var entity = subscriptionRepository.findById(UUID.fromString(id))
                .orElseThrow();

        return null;
    }
}

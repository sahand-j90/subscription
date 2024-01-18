package com.example.subscription.services;

import com.example.subscription.domains.SubscriptionEntity;
import com.example.subscription.repositories.SubscriptionRepository;
import com.example.subscription.services.dto.CreateSubscriptionDto;
import com.example.subscription.services.dto.SubscriptionDto;
import com.example.subscription.services.dto.UpdateSubscriptionDto;
import com.example.subscription.services.mapper.SubscriptionMapper;
import com.example.subscription.services.validators.SubscriptionDateValidation;
import com.example.subscription.services.validators.SubscriptionDeleteValidation;
import com.example.subscription.services.validators.SubscriptionHasOverlapValidation;
import com.example.subscription.services.validators.Validator;
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
    private final SubscriberService subscriberService;

    @Transactional(readOnly = true)
    public SubscriptionDto get(String id) {

        var uuid = UUID.fromString(id);

        var subscription = findEntity(uuid);

        return subscriptionMapper.toDto(subscription);
    }

    @Transactional(rollbackFor = Throwable.class)
    public SubscriptionDto create(CreateSubscriptionDto dto) {

        var subscription = subscriptionMapper.toEntity(dto);
        var subscriber = subscriberService.findEntity(dto.getSubscriberId());
        subscription.setSubscriber(subscriber);

        new Validator()
                .with(new SubscriptionDateValidation(subscription))
                .with(new SubscriptionHasOverlapValidation(subscription, subscriptionRepository))
                .validate();

        subscriptionRepository.save(subscription);

        return subscriptionMapper.toDto(subscription);
    }

    @Transactional(rollbackFor = Throwable.class)
    public SubscriptionDto update(UpdateSubscriptionDto dto) {

        var subscription = findEntity(dto.getId());

        subscriptionMapper.update(dto, subscription);

        new Validator()
                .with(new SubscriptionDateValidation(subscription))
                .with(new SubscriptionHasOverlapValidation(subscription, subscriptionRepository))
                .validate();

        subscriptionRepository.save(subscription);

        return subscriptionMapper.toDto(subscription);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void delete(String id) {

        var uuid = UUID.fromString(id);

        new Validator()
                .with(new SubscriptionDeleteValidation(uuid, subscriptionRepository))
                .validate();

        subscriptionRepository.deleteById(uuid);
    }

    public SubscriptionEntity findEntity(UUID id) {
        return subscriptionRepository.findById(id)
                .orElseThrow();
    }

}

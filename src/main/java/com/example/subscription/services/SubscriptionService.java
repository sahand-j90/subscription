package com.example.subscription.services;

import com.example.subscription.domains.SubscriberEntity_;
import com.example.subscription.domains.SubscriptionEntity;
import com.example.subscription.enums.SubscriptionStateEnum;
import com.example.subscription.exceptions.BizException;
import com.example.subscription.exceptions.Errors;
import com.example.subscription.repositories.SubscriptionRepository;
import com.example.subscription.services.dto.CreateSubscriptionDto;
import com.example.subscription.services.dto.SubscriptionDto;
import com.example.subscription.services.dto.UpdateSubscriptionDto;
import com.example.subscription.services.mapper.SubscriptionMapper;
import com.example.subscription.services.specs.SubscriptionSpecificationBuilder;
import com.example.subscription.services.specs.core.PaginationResult;
import com.example.subscription.services.validators.SubscriptionDateValidation;
import com.example.subscription.services.validators.SubscriptionDeleteValidation;
import com.example.subscription.services.validators.SubscriptionHasOverlapValidation;
import com.example.subscription.services.validators.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Transactional(readOnly = true)
    public PaginationResult<SubscriptionDto> search(SubscriptionSpecificationBuilder.SubscriptionSearch search) {

        var pageNumber = search.getPageNumber();
        var pageSize = search.getPageSize();

        var pageRequest = PageRequest.of(pageNumber , pageSize, Sort.by(Sort.Direction.DESC, SubscriberEntity_.CREATED_AT));
        var spec = new SubscriptionSpecificationBuilder().build(search);

        var page = subscriptionRepository.findAll(spec, pageRequest);
        var items = subscriptionMapper.toDto(page.getContent());

        return PaginationResult.<SubscriptionDto>builder()
                .currentPage(pageNumber)
                .totalItems(page.getTotalElements())
                .pageSize(pageSize)
                .items(items)
                .build();
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

        resolveSubscriptionState(subscription);

        subscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toDto(subscription);
    }

    @Transactional(rollbackFor = Throwable.class)
    public SubscriptionDto update(UpdateSubscriptionDto dto) {

        var subscription = findEntity(dto.getId())
                .toBuilder().build();

        subscriptionMapper.update(dto, subscription);

        new Validator()
                .with(new SubscriptionDateValidation(subscription))
                .with(new SubscriptionHasOverlapValidation(subscription, subscriptionRepository))
                .validate();

        subscription = subscriptionRepository.save(subscription);

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
                .orElseThrow(() -> new BizException(Errors.SUBSCRIBER_NOT_FOUND));
    }

    private void resolveSubscriptionState(SubscriptionEntity subscription) {

        SubscriptionStateEnum subscriptionState;

        if (LocalDate.now().isBefore(subscription.getFrom())) {
            subscriptionState = SubscriptionStateEnum.RESERVED;
        }
        else {
            subscriptionState = SubscriptionStateEnum.ACTIVE;
        }

        subscription.setState(subscriptionState);
    }

}

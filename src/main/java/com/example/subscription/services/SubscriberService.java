package com.example.subscription.services;

import com.example.subscription.domains.SubscriberEntity;
import com.example.subscription.domains.SubscriberEntity_;
import com.example.subscription.repositories.SubscriberRepository;
import com.example.subscription.services.dto.CreateSubscriberDto;
import com.example.subscription.services.dto.SubscriberDto;
import com.example.subscription.services.dto.UpdateSubscriberDto;
import com.example.subscription.services.mapper.SubscriberMapper;
import com.example.subscription.services.specs.SubscriberSpecificationBuilder;
import com.example.subscription.services.specs.core.PaginationResult;
import com.example.subscription.services.validators.SubscriberNameAlreadyExistsValidation;
import com.example.subscription.services.validators.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SubscriberMapper subscriberMapper;

    @Transactional(readOnly = true)
    public SubscriberDto get(String id) {

        var uuid = UUID.fromString(id);

        var entity = findEntity(uuid);

        return subscriberMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public PaginationResult<SubscriberDto> search(SubscriberSpecificationBuilder.SubscriberSearch search) {

        var pageNumber = search.getPageNumber();
        var pageSize = search.getPageSize();

        var pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, SubscriberEntity_.CREATED_AT));
        var spec = new SubscriberSpecificationBuilder().build(search);
        var page = subscriberRepository.findAll(spec, pageRequest);
        var items = subscriberMapper.toDto(page.getContent());

        return PaginationResult.<SubscriberDto>builder()
                .currentPage(pageNumber)
                .totalItems(page.getTotalElements())
                .pageSize(pageSize)
                .items(items)
                .build();
    }

    @Transactional(rollbackFor = Throwable.class)
    public SubscriberDto create(CreateSubscriberDto dto) {

        var subscriber = subscriberMapper.toEntity(dto);

        new Validator()
                .with(new SubscriberNameAlreadyExistsValidation(subscriber.getSubscriberName(), subscriberRepository))
                .validate();

        subscriberRepository.save(subscriber);

        return subscriberMapper.toDto(subscriber);
    }

    @Transactional(rollbackFor = Throwable.class)
    public SubscriberDto update(UpdateSubscriberDto dto) {

        var subscriber = subscriberRepository.findById(dto.getId())
                .orElseThrow();

        if (!subscriber.getSubscriberName().equals(dto.getSubscriberName())) {
            new Validator()
                    .with(new SubscriberNameAlreadyExistsValidation(dto.getSubscriberName(), subscriberRepository))
                    .validate();
        }

        subscriberMapper.update(dto, subscriber);
        subscriberRepository.save(subscriber);

        return subscriberMapper.toDto(subscriber);
    }


    public SubscriberEntity findEntity(UUID id) {
        return subscriberRepository.findById(id)
                .orElseThrow();
    }
}

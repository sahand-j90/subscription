package com.example.subscription.services;

import com.example.subscription.repositories.SubscriberRepository;
import com.example.subscription.services.dto.CreateSubscriberDto;
import com.example.subscription.services.dto.SubscriberDto;
import com.example.subscription.services.dto.UpdateSubscriberDto;
import com.example.subscription.services.mapper.SubscriberMapper;
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
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SubscriberMapper subscriberMapper;

    @Transactional(readOnly = true)
    public SubscriberDto get(String id) {
        var entity = subscriberRepository.findById(UUID.fromString(id))
                .orElseThrow();

        return subscriberMapper.toDto(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public SubscriberDto create(CreateSubscriberDto dto) {

        var entity = subscriberMapper.toEntity(dto);

        subscriberRepository.save(entity);

        return subscriberMapper.toDto(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public SubscriberDto create(UpdateSubscriberDto dto) {

        subscriberRepository.findById(dto.getId())
                .orElseThrow();

        var entity = subscriberMapper.toEntity(dto);

        subscriberRepository.save(entity);

        return subscriberMapper.toDto(entity);
    }

}

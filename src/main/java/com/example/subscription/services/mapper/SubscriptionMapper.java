package com.example.subscription.services.mapper;

import com.example.subscription.domains.SubscriberEntity;
import com.example.subscription.services.dto.SubscriptionDto;
import org.mapstruct.Mapper;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Mapper(componentModel = "spring")
interface SubscriptionMapper extends EntityMapper<SubscriptionDto, SubscriberEntity> {
}


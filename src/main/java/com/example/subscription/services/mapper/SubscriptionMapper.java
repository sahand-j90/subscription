package com.example.subscription.services.mapper;

import com.example.subscription.domains.SubscriptionEntity;
import com.example.subscription.services.dto.CreateSubscriptionDto;
import com.example.subscription.services.dto.SubscriptionDto;
import com.example.subscription.services.dto.UpdateSubscriptionDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Mapper(componentModel = "spring")
public interface SubscriptionMapper extends EntityMapper<SubscriptionDto, SubscriptionEntity> {

    SubscriptionEntity toEntity(CreateSubscriptionDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateSubscriptionDto dto, @MappingTarget SubscriptionEntity entity);
}

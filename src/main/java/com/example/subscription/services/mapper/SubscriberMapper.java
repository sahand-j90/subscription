package com.example.subscription.services.mapper;

import com.example.subscription.domains.SubscriberEntity;
import com.example.subscription.services.dto.CreateSubscriberDto;
import com.example.subscription.services.dto.SubscriberDto;
import com.example.subscription.services.dto.UpdateSubscriberDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Mapper(componentModel = "spring")
public interface SubscriberMapper extends EntityMapper<SubscriberDto, SubscriberEntity> {

    SubscriberEntity toEntity(CreateSubscriberDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateSubscriberDto dto, @MappingTarget SubscriberEntity entity);
}

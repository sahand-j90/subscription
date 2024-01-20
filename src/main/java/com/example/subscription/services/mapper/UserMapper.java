package com.example.subscription.services.mapper;

import com.example.subscription.domains.UserEntity;
import com.example.subscription.services.dto.CreateUserDto;
import com.example.subscription.services.dto.UpdateUserDto;
import com.example.subscription.services.dto.UserDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, UserEntity> {

    UserEntity toEntity(CreateUserDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateUserDto dto, @MappingTarget UserEntity entity);

}

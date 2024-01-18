package com.example.subscription.services.mapper;

import java.util.List;

/**
 * @author Sahand Jalilvand 18.01.24
 */
public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);
}

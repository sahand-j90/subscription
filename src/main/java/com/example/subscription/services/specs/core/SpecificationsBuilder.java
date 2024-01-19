package com.example.subscription.services.specs.core;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Sahand Jalilvand 19.01.24
 */
public abstract class SpecificationsBuilder<T, E> {

    protected List<SearchCriteria> params;

    public SpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public SpecificationsBuilder<T, E> with(String key, SearchOperation operation, Object value, String join) {

        if (operation != null) {
            if (SearchOperation.LIKE == operation) {
                params.add(new SearchCriteria(key, operation, "%" + value + "%", join));
            } else if (SearchOperation.STARTS_WITH == operation) {
                params.add(new SearchCriteria(key, operation, value + "%", join));
            } else if (SearchOperation.ENDS_WITH == operation) {
                params.add(new SearchCriteria(key, operation, "%" + value, join));
            } else {
                params.add(new SearchCriteria(key, operation, value, join));
            }
        }
        return this;
    }

    public abstract Specification<E> build(T search);

    protected Specification<E> build(Function<SearchCriteria, EntitySpecification> function) {

        if (params.size() == 0) {
            return null;
        }

        Specification result = function.apply(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(function.apply(params.get(i)));
        }

        return result;
    }
}


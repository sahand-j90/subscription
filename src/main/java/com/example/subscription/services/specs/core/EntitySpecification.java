package com.example.subscription.services.specs.core;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Sahand Jalilvand 19.01.24
 */
public class EntitySpecification<T> implements Specification<T> {

    private final SearchCriteria criteria;

    public EntitySpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(getExpression(root), criteria.getValue());
            case NEGATION:
                return builder.notEqual(getExpression(root), criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(getExpression(root), (Comparable) criteria.getValue());
            case GREATER_THAN_OR_EQUAL:
                return builder.greaterThanOrEqualTo(getExpression(root), (Comparable) criteria.getValue());
            case LESS_THAN:
                return builder.lessThan(getExpression(root), (Comparable) criteria.getValue());
            case LESS_THAN_OR_EQUAL:
                return builder.lessThanOrEqualTo(getExpression(root), (Comparable) criteria.getValue());
            case LIKE:
            case STARTS_WITH:
            case ENDS_WITH:
                return builder.like(getExpression(root), criteria.getValue().toString());
            case CONTAINS:
                return getExpression(root).in(convertToList(criteria.getValue()));
            default:
                return null;
        }
    }

    private <E> Path<E> getExpression(Root<T> root) {
        if (criteria.getJoin() != null) {
            Join<Object, Object> join = getExpression(root, criteria.getJoin());
            return join.get(criteria.getKey());
        }
        return root.get(criteria.getKey());
    }

    private Join<Object, Object> getExpression(Root<T> root, String joinExpression) {
        var paths = joinExpression.split("\\.");
        Join<Object, Object> join = root.join(paths[0]);
        for (int i = 1; i < paths.length; i++) {
            join = join.join(paths[i]);
        }
        return join;
    }

    private List<?> convertToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }
}


package com.example.subscription.services.specs;

import com.example.subscription.domains.UserEntity;
import com.example.subscription.services.specs.core.EntitySpecification;
import com.example.subscription.services.specs.core.PaginationSearch;
import com.example.subscription.services.specs.core.SpecificationsBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Sahand Jalilvand 20.01.24
 */
public class UserSpecificationBuilder extends SpecificationsBuilder<UserSpecificationBuilder.UserSearch, UserEntity> {

    @Override
    public Specification<UserEntity> build(UserSearch search) {

        return super.build(EntitySpecification::new);
    }

    @Data
    @AllArgsConstructor
    public static class UserSearch extends PaginationSearch {
    }
}

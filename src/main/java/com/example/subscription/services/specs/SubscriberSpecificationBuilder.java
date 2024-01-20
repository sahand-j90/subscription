package com.example.subscription.services.specs;

import com.example.subscription.domains.SubscriberEntity;
import com.example.subscription.domains.SubscriberEntity_;
import com.example.subscription.services.specs.core.EntitySpecification;
import com.example.subscription.services.specs.core.PaginationSearch;
import com.example.subscription.services.specs.core.SearchOperation;
import com.example.subscription.services.specs.core.SpecificationsBuilder;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Sahand Jalilvand 19.01.24
 */
public class SubscriberSpecificationBuilder extends SpecificationsBuilder<SubscriberSpecificationBuilder.SubscriberSearch, SubscriberEntity> {


    @Override
    public Specification<SubscriberEntity> build(SubscriberSearch search) {

        if (StringUtils.isNotBlank(search.getSubscriberName())) {
            with(SubscriberEntity_.SUBSCRIBER_NAME, SearchOperation.LIKE, search.getSubscriberName(), null);
        }

        return super.build(EntitySpecification::new);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriberSearch extends PaginationSearch {

        private String subscriberName;
    }

}

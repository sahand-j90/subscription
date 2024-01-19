package com.example.subscription.services.specs;

import com.example.subscription.domains.SubscriberEntity_;
import com.example.subscription.domains.SubscriptionEntity;
import com.example.subscription.domains.SubscriptionEntity_;
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
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 19.01.24
 */
public class SubscriptionSpecificationBuilder extends SpecificationsBuilder<SubscriptionSpecificationBuilder.SubscriptionSearch, SubscriptionEntity> {

    @Override
    public Specification<SubscriptionEntity> build(SubscriptionSearch search) {

        if (StringUtils.isNotBlank(search.getSubscriberId())) {
            var uuid = UUID.fromString(search.getSubscriberId());
            with(SubscriberEntity_.ID, SearchOperation.EQUALITY, uuid, SubscriptionEntity_.SUBSCRIBER);
        }

        if (search.getFrom() != null) {
            with(SubscriptionEntity_.FROM, SearchOperation.GREATER_THAN_OR_EQUAL, search.getFrom(), null);
        }

        if (search.getTo() != null) {
            with(SubscriptionEntity_.TO, SearchOperation.LESS_THAN_OR_EQUAL, search.getTo(), null);
        }


        return super.build(EntitySpecification::new);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriptionSearch extends PaginationSearch {
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate from;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate to;

        @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "فرمت ایدی اشتباه است")
        private String subscriberId;
    }
}

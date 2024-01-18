package com.example.subscription.outbox;

import com.example.subscription.domains.SubscriptionEntity;
import com.example.subscription.enums.SubscriptionEventTypeEnum;
import com.example.subscription.enums.SubscriptionStateEnum;
import com.example.subscription.outbox.core.AbstractPostInsertListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Component
@Slf4j
public class SubscriptionPostInsertListener extends AbstractPostInsertListener<SubscriptionEntity> {

    public SubscriptionPostInsertListener(ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    public Class<SubscriptionEntity> getType() {
        return SubscriptionEntity.class;
    }

    @Override
    protected List<DomainEvent<?>> onPostInsert(SubscriptionEntity subscriptionEntity) {

        var events = new ArrayList<DomainEvent<?>>();

        creationDomainEvent(subscriptionEntity)
                .ifPresent(events::add);

        activationDomainEvent(subscriptionEntity)
                .ifPresent(events::add);

        return events;
    }

    private Optional<DomainEvent<SubscriptionEntity>> creationDomainEvent(SubscriptionEntity subscriptionEntity) {
        var domainEvent = DomainEvent.<SubscriptionEntity>builder()
                .correlationId(subscriptionEntity.getId().toString())
                .domain(SubscriptionEntity.class.getSimpleName())
                .eventType(SubscriptionEventTypeEnum.CREATED.name())
                .payload(subscriptionEntity)
                .build();

        return Optional.of(domainEvent);
    }

    private Optional<DomainEvent<SubscriptionEntity>> activationDomainEvent(SubscriptionEntity subscriptionEntity) {
        if (SubscriptionStateEnum.ACTIVE == subscriptionEntity.getState()) {
            var domainEvent = DomainEvent.<SubscriptionEntity>builder()
                    .correlationId(subscriptionEntity.getId().toString())
                    .domain(SubscriptionEntity.class.getSimpleName())
                    .eventType(SubscriptionEventTypeEnum.ACTIVATED.name())
                    .payload(subscriptionEntity)
                    .build();

            return Optional.of(domainEvent);
        } else {
            return Optional.empty();
        }
    }
}

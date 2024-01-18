package com.example.subscription.outbox;

import com.example.subscription.domains.SubscriptionEntity;
import com.example.subscription.domains.SubscriptionEntity_;
import com.example.subscription.enums.SubscriptionEventTypeEnum;
import com.example.subscription.enums.SubscriptionStateEnum;
import com.example.subscription.outbox.core.AbstractPostUpdateListener;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Component
@Slf4j
public class SubscriptionPostUpdateListener extends AbstractPostUpdateListener<SubscriptionEntity> {

    public SubscriptionPostUpdateListener(EntityManagerFactory entityManagerFactory, ApplicationEventPublisher eventPublisher) {
        super(entityManagerFactory, eventPublisher);
    }

    @Override
    public Class<SubscriptionEntity> getType() {
        return SubscriptionEntity.class;
    }

    @Override
    protected List<DomainEvent<?>> onPostUpdate(SubscriptionEntity subscriptionEntity, Predicate<String> isPropertyChanged, Function<String, Object> getOldValue) {

        var events = new ArrayList<DomainEvent<?>>();

        stateChangedDomainEvent(subscriptionEntity, isPropertyChanged)
                .ifPresent(events::add);

        return events;
    }

    private Optional<DomainEvent<SubscriptionEntity>> stateChangedDomainEvent(SubscriptionEntity subscriptionEntity, Predicate<String> isPropertyChanged) {
        if (isPropertyChanged.test(SubscriptionEntity_.STATE) && SubscriptionStateEnum.RESERVED != subscriptionEntity.getState()) {

            var eventType = getEventType(subscriptionEntity.getState());

            var domainEvent = DomainEvent.<SubscriptionEntity>builder()
                    .correlationId(subscriptionEntity.getId().toString())
                    .domain(SubscriptionEntity.class.getSimpleName())
                    .eventType(eventType.name())
                    .payload(subscriptionEntity)
                    .build();

            return Optional.of(domainEvent);
        } else {
            return Optional.empty();
        }
    }

    private SubscriptionEventTypeEnum getEventType(SubscriptionStateEnum state) {
        return switch (state) {
            case ACTIVE -> SubscriptionEventTypeEnum.ACTIVATED;
            case FINISHED -> SubscriptionEventTypeEnum.EXPIRED;
            default -> throw new IllegalArgumentException();
        };
    }
}

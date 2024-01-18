package com.example.subscription.outbox.core;

import com.example.subscription.outbox.DomainEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

/**
 * @author Sahand Jalilvand 16.01.24
 */
public abstract class AbstractPostInsertListener<T> {

    private final ApplicationEventPublisher eventPublisher;

    public AbstractPostInsertListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public abstract Class<T> getType();

    public final void onPostInsert(PostInsertEvent event) {
        var domainEvents = onPostInsert((T) event.getEntity(), event);
        domainEvents.stream()
                .peek(domainEvent -> domainEvent.setSession(event.getSession()))
                .forEach(eventPublisher::publishEvent);
    }

    protected abstract List<DomainEvent<?>> onPostInsert(T t, PostInsertEvent event);

}

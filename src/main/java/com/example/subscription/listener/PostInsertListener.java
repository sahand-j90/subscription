package com.example.subscription.listener;

import com.example.subscription.listener.DomainEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

/**
 * @author Sahand Jalilvand 16.01.24
 */
public abstract class PostInsertListener<T> {

    private final ApplicationEventPublisher eventPublisher;

    public PostInsertListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public abstract Class<T> getType();

    public final void onPostInsert(PostInsertEvent event) {
        var domainEvents = onPostInsert((T) event.getEntity(), event);
        domainEvents.stream().peek(domainEvent -> domainEvent.setSession(event.getSession()))
                .forEach(eventPublisher::publishEvent);
    }

    protected abstract List<DomainEvent<?>> onPostInsert(T t, PostInsertEvent event);

}

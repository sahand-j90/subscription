package com.example.subscription.listener.outbox;

import com.example.subscription.listener.DomainEvent;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Sahand Jalilvand 16.01.24
 */
public abstract class PostUpdateListener<T> {

    private ApplicationEventPublisher eventPublisher;
    private final Map<String, Integer> propertyIndexMap;

    public PostUpdateListener(EntityManagerFactory entityManagerFactory, ApplicationEventPublisher eventPublisher) {

        this.eventPublisher = eventPublisher;
        this.propertyIndexMap = new HashMap<>();

        fillPropertyIndexMap(entityManagerFactory);
    }

    public abstract Class<T> getType();

    public final void onPostUpdate(final PostUpdateEvent event) {

        Predicate<String> isPropertyChangedClosure = propertyName -> isPropertyChanged(event, propertyName);

        Function<String, Object> getOldValueClosure = propertyName -> getOldValue(event, propertyName);

        var domainEvents = onPostUpdate((T) event.getEntity(), isPropertyChangedClosure, getOldValueClosure);

        domainEvents.stream()
                .peek(domainEvent -> domainEvent.setSession(event.getSession()))
                .forEach(eventPublisher::publishEvent);
    }

    protected abstract List<DomainEvent<?>> onPostUpdate(T t,  Predicate<String> isPropertyChanged, Function<String, Object> getOldValue);

    private boolean isPropertyChanged(PostUpdateEvent event, final String propertyName) {

        var indexOfProperty = propertyIndexMap.get(propertyName);
        var dirtyProperties = event.getDirtyProperties();

        for (int dirtyPropertyIndex : dirtyProperties) {

            var isChanged = indexOfProperty == dirtyPropertyIndex;

            if (isChanged) {

                var newValue = event.getState()[indexOfProperty];
                var oldValue = event.getOldState()[indexOfProperty];

                if (newValue == null && oldValue == null) {
                    return false;
                } else if (newValue == null) {
                    return true;
                } else {
                    return !newValue.equals(oldValue);
                }
            }
        }

        return false;
    }

    private Object getOldValue(PostUpdateEvent event, String propertyName) {
        var indexOfProperty = propertyIndexMap.get(propertyName);
        return event.getOldState()[indexOfProperty];
    }

    private void fillPropertyIndexMap(EntityManagerFactory entityManagerFactory) {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EntityPersister persister = sessionFactory.getMetamodel().getEntityDescriptor(getType());
        var propertyNames = persister.getPropertyNames();
        for (int i = 0; i < propertyNames.length; i++) {
            propertyIndexMap.put(propertyNames[i], i);
        }
    }
}
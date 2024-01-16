package com.example.subscription.listener;

import com.example.subscription.listener.DomainEvent;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public final void onPostUpdate(PostUpdateEvent event) {
        var domainEvents = onPostUpdate((T) event.getEntity(), event);
        domainEvents.forEach(eventPublisher::publishEvent);
    }

    protected abstract List<DomainEvent<?>> onPostUpdate(T t, PostUpdateEvent event);

    protected final boolean isPropertyChanged(PostUpdateEvent event, final String propertyName, Object newValue) {

        var indexOfProperty = propertyIndexMap.get(propertyName);
        var dirtyProperties = event.getDirtyProperties();

        for (int dirtyPropertyIndex : dirtyProperties) {

            var isChanged = indexOfProperty == dirtyPropertyIndex;

            if (isChanged) {

                var oldValue = getOldValue(event, propertyName);

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

    protected final Object getOldValue(PostUpdateEvent event, String propertyName) {
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
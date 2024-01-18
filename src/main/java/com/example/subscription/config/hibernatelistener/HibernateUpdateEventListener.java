package com.example.subscription.config.hibernatelistener;

import com.example.subscription.listener.outbox.AbstractPostUpdateListener;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

import java.util.List;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@RequiredArgsConstructor
public class HibernateUpdateEventListener implements PostUpdateEventListener {

    private final List<AbstractPostUpdateListener<?>> abstractPostUpdateListeners;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        abstractPostUpdateListeners.stream()
                .filter(i -> i.getType().isAssignableFrom(event.getEntity().getClass()))
                .forEach(i -> i.onPostUpdate(event));
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister entityPersister) {
        return false;
    }
}
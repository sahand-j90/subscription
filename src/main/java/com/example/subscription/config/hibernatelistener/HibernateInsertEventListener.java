package com.example.subscription.config.hibernatelistener;

import com.example.subscription.listener.outbox.AbstractPostInsertListener;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;

import java.util.List;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@RequiredArgsConstructor
public class HibernateInsertEventListener implements PostInsertEventListener {

    private final List<AbstractPostInsertListener<?>> abstractPostInsertListenerList;

    @Override
    public void onPostInsert(PostInsertEvent event) {
        abstractPostInsertListenerList.stream()
                .filter(i -> i.getType().isAssignableFrom(event.getEntity().getClass()))
                .forEach(i -> i.onPostInsert(event));
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister entityPersister) {
        return false;
    }
}
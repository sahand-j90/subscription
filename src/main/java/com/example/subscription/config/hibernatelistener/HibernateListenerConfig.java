package com.example.subscription.config.hibernatelistener;

import com.example.subscription.listener.outbox.PostInsertListener;
import com.example.subscription.listener.outbox.PostUpdateListener;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class HibernateListenerConfig implements InitializingBean {


    private final EntityManagerFactory entityManagerFactory;

    private final List<PostInsertListener<?>> postInsertListenerList;

    private final List<PostUpdateListener<?>> postUpdateListeners;


    @Override
    public void afterPropertiesSet() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.POST_INSERT)
                .appendListener(hibernateInsertEventListener(postInsertListenerList));

        registry.getEventListenerGroup(EventType.POST_UPDATE)
                .appendListener(hibernateUpdateEventListener(postUpdateListeners));
    }

    private PostInsertEventListener hibernateInsertEventListener(List<PostInsertListener<?>> postInsertListenerList) {
        return new HibernateInsertEventListener(postInsertListenerList);
    }

    private PostUpdateEventListener hibernateUpdateEventListener(List<PostUpdateListener<?>> postUpdateListeners) {
        return new HibernateUpdateEventListener(postUpdateListeners);
    }

}

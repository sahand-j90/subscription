package com.example.subscription.config.ormlistener;

import com.example.subscription.outbox.core.AbstractPostInsertListener;
import com.example.subscription.outbox.core.AbstractPostUpdateListener;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
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
    private final List<AbstractPostInsertListener<?>> abstractPostInsertListenerList;
    private final List<AbstractPostUpdateListener<?>> abstractPostUpdateListeners;


    @Override
    public void afterPropertiesSet() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.POST_INSERT)
                .appendListener(new HibernateInsertEventListener(abstractPostInsertListenerList));

        registry.getEventListenerGroup(EventType.POST_UPDATE)
                .appendListener(new HibernateUpdateEventListener(abstractPostUpdateListeners));
    }

}

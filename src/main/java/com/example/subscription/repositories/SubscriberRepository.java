package com.example.subscription.repositories;

import com.example.subscription.domains.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, UUID> {

    Optional<SubscriberEntity> findBySubsAndSubscriberName(String subscriberName);
}

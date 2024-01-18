package com.example.subscription.repositories;

import com.example.subscription.domains.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {
}

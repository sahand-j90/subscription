package com.example.subscription.repositories;

import com.example.subscription.domains.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Sahand Jalilvand 16.01.24
 */
public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {
}

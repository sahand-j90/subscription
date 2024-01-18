package com.example.subscription.repositories;

import com.example.subscription.domains.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, UUID> {
}

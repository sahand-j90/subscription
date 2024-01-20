package com.example.subscription.repositories;

import com.example.subscription.domains.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 20.01.24
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);
}

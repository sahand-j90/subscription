package com.example.subscription.repositories;

import com.example.subscription.domains.SubscriptionEntity;
import com.example.subscription.enums.SubscriptionStateEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID>, JpaSpecificationExecutor<SubscriptionEntity> {

    @Query("select s from SubscriptionEntity s " +
            "where s.subscriber.id = :subscriberId and " +
            "((s.from >= :from and s.from <= :to) or (s.to >= :from and s.to <= :to))")
    List<SubscriptionEntity> findAllBetweenDates(UUID subscriberId, LocalDate from, LocalDate to);

    List<SubscriptionEntity> findAllByStateAndToIsBefore(SubscriptionStateEnum state, LocalDate toDate, Pageable pageable);

    List<SubscriptionEntity> findAllByStateAndFrom(SubscriptionStateEnum state, LocalDate toDate, Pageable pageable);
}

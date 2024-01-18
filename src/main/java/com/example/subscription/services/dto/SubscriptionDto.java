package com.example.subscription.services.dto;

import com.example.subscription.enums.SubscriptionStateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDto {

    private UUID id;

    private int version;

    private Date createdAt;

    private Date updatedAt;

    private SubscriptionStateEnum state;

    private LocalDate from;

    private LocalDate to;

    private UUID subscriberId;

}
